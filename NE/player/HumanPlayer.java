package NE.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.card.Card.CardCategory;
import NE.display.Display;
import NE.main.GameManager;

public class HumanPlayer extends Player {

    private static int id = 1;

    public HumanPlayer(Board board) {
        super(board);
        this.name = "Player-" + id;
        id++;
    }

    @Override
    public List<Integer> askDiscard(Board board, int cost, List<Integer> indexesNotAllowed) {
        return this.askDiscard(board, cost);
    }

    @Override
    public List<Integer> askDiscard(Board board, int cost) {
        System.out.println("捨てるカードを" + cost + "枚選んでください");
        Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < cost) {
            indexes.add(Display.scanNextInt(this.hands.size()));
        }
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> askBuild(Board board, int amounts, Card card) {
        System.out.println("建設するカードを選んでください");
        Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < amounts) {
            indexes.add(Display.scanNextInt(this.hands.size()));
        }
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public void processTurn(Board board) {
        Card cardToWork = null;
        boolean done = false;
        do {
            // 選択肢を表示
            System.out.println();
            System.out.println("1 => 公共フィールド");
            System.out.println(board.getBuildings());
            System.out.println("2 => 自フィールド");
            System.out.println(this.buildings);
            System.out.println();

            // 労働者を働かせるエリアを選ぶ。まず二択
            int optionA = Display.scanNextInt(2);
            List<Card> area = new ArrayList<>();
            switch (optionA) {
                case 0:
                    area = board.getBuildings();
                    Display.printChoices(board.getBuildings());
                    break;
                case 1:
                    area = this.buildings;
                    Display.printChoices(this.buildings);
                    break;
            }

            if (area.isEmpty()) {
                System.out.println("選択可能なカードが存在しません");
                continue;
            }

            // 具体的なカードの選択を受け取る
            int optionB = Display.scanNextInt(area.size());

            // 選択されたカードを取得
            cardToWork = area.get(optionB);

            // カードを使用する
            done = cardToWork.apply(this, board);
            if (!done) {
                System.out.println("丸投げだけど、何らかの理由で使用できません。もう一度最初から");
            }
        } while (!done);
        addHistory(cardToWork);

    }

    @Override
    public void payWages(Board board) {

        // トータル賃金
        int totalWages = this.getWorkers().size() * GameManager.getCurrentWage();

        // 不足金額
        int deficit = totalWages - this.getMoney();
        System.out.println("deficit: " + deficit);

        List<Card> buildingsSold = new ArrayList<>();

        // Beforeを保存
        List<Card> buildingsBefore = new ArrayList<>(this.getBuildings());// 丸ごとクローンして保存
        int moneyBefore = this.getMoney();

        while (true) {
            // 賃金を所持金でまかなえなければ所持物件を売る
            while (this.getMoney() < totalWages
                    && this.getBuildings().stream().filter(c -> c.getCategory() != CardCategory.FACILITY).count() > 0) {

                Card buildingToSell = null;
                int option;

                System.out.println("売却するカードを選択してください");
                Display.printChoices(this.getBuildings());
                option = Display.scanNextInt(this.getBuildings().size());

                buildingToSell = this.getBuildings().get(option);

                if (buildingToSell.getCategory() == CardCategory.FACILITY)
                    continue;

                buildingsSold.add(buildingToSell);
                this.sellBuildings(board, buildingToSell);
            }

            System.out.println("buildingsSold " + buildingsSold);

            // 売ったカードをチェックする
            // まず売った建物を価値で昇順にソート
            List<Card> cardsToSellOrdered = buildingsSold.stream()
                    .sorted(Comparator.comparing(Card::getValue, Comparator.naturalOrder()))
                    .collect(Collectors.toList());

            System.out.println("cardsToSellOrdered " + cardsToSellOrdered);

            boolean isOK = true;
            int totalValue = 0;
            // 売ったカードの価格を一つ一つ足していき、リストがすべて終わる前に不足金額に達したら不正
            for (int i = cardsToSellOrdered.size() - 1; i >= 0; i--) {
                totalValue += GameManager.getInstance().isSinglePlay() ? cardsToSellOrdered.get(i).getValue() / 2
                        : cardsToSellOrdered.get(i).getValue();
                if (totalValue >= deficit && i > 0) {
                    isOK = false;
                    break;
                }
            }

            if (isOK) {
                break;
            }
            System.out.println("売却物件が不正です。もう一度やり直してください");
            // 巻き戻し処理
            // 公共エリアから消す
            board.getBuildings().removeAll(cardsToSellOrdered);
            // 持ち物件に加える
            // this.getBuildings().addAll(cardsToSell);
            // 持ち物件を戻す。ただし、参照が変わる。もしほかの場所で参照を保持しているならバグが出る
            this.setBuildings(buildingsBefore);
            // 所持金を戻す
            this.setMoney(moneyBefore);

            buildingsSold.clear();
        }

        // 賃金を支払う
        this.payMoney(board, totalWages);

    }
}