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
import NE.player.ai.IAI;

public class AIPlayer extends Player {

    private IAI brain;

    private static int id = 1;

    public AIPlayer(Board board, IAI brain) {
        super(board);
        this.brain = brain;
        this.name = "AI-" + id + ":BRAIN=" + this.brain.getClass().getName() + " INFO=" + this.brain.toString();
        id++;
    }

    public IAI getBrain() {
        return brain;
    }

    public void setBrain(IAI brain) {
        this.brain = brain;
    }

    @Override
    public List<Integer> askDiscard(Board board, int cost, List<Integer> indexesNotAllowed) {

        System.out.println("捨てるカードを" + cost + "枚選んでください");
        if (GameManager.isAITransparent())
            Display.printChoices(this.hands);
        // 許可されていないindexを最初に入れてしまう
        Set<Integer> indexes = new HashSet<>(indexesNotAllowed);
        System.out.println("NOT ALLOWED: " + indexes);
        int count = 0;
        while (indexes.size() < cost + indexesNotAllowed.size()) {
            indexes.add(this.brain.thinkDiscard(this, board, indexes));
            count++;
            if (count >= 10) {
                // TODO AIのstuckを予防する
                System.out.println("infinite loop detected... Auto-piloting initiated");

                break;
            }
        }
        System.out.println("NOT ALLOWED + AI CHOISE: " + indexes);

        /// 許可されていないindexを削除する
        for (Integer integer : indexesNotAllowed) {
            indexes.remove(integer);
        }
        System.out.println("AI chose: " + indexes);
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> askDiscard(Board board, int cost) {

        System.out.println("捨てるカードを" + cost + "枚選んでください");
        if (GameManager.isAITransparent())
            Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        int count = 0;
        while (indexes.size() < cost) {
            indexes.add(this.brain.thinkDiscard(this, board, indexes));
            count++;
            if (count >= 10) {
                // TODO AIのstuckを予防する
                System.out.println("infinite loop detected... Auto-piloting initiated");

                break;
            }
        }
        System.out.println("AI chose: " + indexes);
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> askBuild(Board board, int amounts, Card card) {
        System.out.println("建設するカードを選んでください");
        if (GameManager.isAITransparent())
            Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        int count = 0;
        while (indexes.size() < amounts) {
            indexes.add(this.brain.thinkBuild(this, board, indexes));
            count++;
            if (count >= 10) {
                // TODO AIのstuckを予防する
                System.out.println("infinite loop detected... Auto-piloting initiated");

                break;
            }
        }
        System.out.println("AI chose: " + indexes);
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public void processTurn(Board board) {
        Card cardToWork = null;
        boolean done = false;
        int stuck = -1;
        do {
            stuck++;
            if (stuck >= GameManager.getMaxStucks()) {
                // 強制で鉱山を使わせる
                // このブロックを下に置くとcontinueの時に実行されず無限ループ
                // TODO
                System.out.println("stuck...");
                System.out.println("forced-piloting initiated");
                board.getBuildings().get(0).apply(this, board);
                break;
            }

            List<Integer> options = this.brain.thinkUseCard(this, board, stuck);
            System.out.println("AI wants to use the card at: " + options);

            int areaChoice = options.get(0);
            List<Card> area;
            switch (areaChoice) {
                case 0:
                    area = board.getBuildings();
                    if (GameManager.isAITransparent())
                        Display.printChoices(board.getBuildings());
                    break;
                case 1:
                    area = this.buildings;
                    if (GameManager.isAITransparent())
                        Display.printChoices(this.buildings);
                    break;
                default:
                    area = new ArrayList<>();
            }

            if (area.isEmpty()) {
                continue;
            }
            // 選択されたエリアからカードを取得
            try {
                cardToWork = area.get(options.get(1));
                System.out.println(cardToWork);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Bad AI! auto-piloting initiated");
                cardToWork = board.getBuildings().get(0);
            }

            try {
                // カードを使用する
                done = cardToWork.apply(this, board);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            System.out.println("AttemptSuccess? " + done);
            try {
                Thread.sleep(GameManager.getWaitTime());
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }

        } while (!done);
        addHistory(cardToWork);
        if (GameManager.isPauseOnAITurn()) {
            System.out.println();
            System.out.println("AIの行動を確認できるよう一時停止しています");
            System.out.println("解除するには 1 を入力してください");
            Display.scanNextInt(1);
        }
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

        int outerCount = 0;
        while (true) {
            int innerCount = 0;
            // 賃金を所持金でまかなえなければ所持物件を売る
            while (this.getMoney() < totalWages
                    && this.getBuildings().stream().filter(c -> c.getCategory() != CardCategory.FACILITY).count() > 0) {

                Card buildingToSell = null;
                int option;

                // todo stuckの可能性あり。とりあえずランダムで抜けられる
                if (innerCount > 10 || outerCount >= 1) {// 恣意的な基準
                    option = new Random().nextInt(this.getBuildings().size());
                    System.out.println("infinite loop detected... Auto-piloting initiated");
                } else {
                    option = this.getBrain().thinkSell(this, board);
                }
                buildingToSell = this.getBuildings().get(option);

                if (buildingToSell.getCategory() == CardCategory.FACILITY)
                    continue;

                buildingsSold.add(buildingToSell);
                this.sellBuildings(board, buildingToSell);
                innerCount++;
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

            outerCount++;

        }

        // 賃金を支払う
        this.payMoney(board, totalWages);

    }

}