package NE.card.construction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class ConstructionMedium extends ConstructionCard {

    public ConstructionMedium() {
        this.id = 11;
        this.category = CardCategory.CONSTRUCTION;
        this.name = "建設中";
        this.cost = 2;
        this.value = 10;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;

        this.minHands = 1;
        this.amountsToBuild = 1;
    }

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {
        try {
            List<Card> hands = player.getHands();

            System.out.println("aaaaaaaaa");

            if (hands.size() < this.minHands || this.isWorked)
                return false;

            // 建てるカードを取得
            int num = (int) options.get(0);
            Card cardToBuild = hands.get(num);
            int cost = cardToBuild.getCost();
            System.out.println("aaaaaaaaa");

            if (cost > hands.size() - this.amountsToBuild || !cardToBuild.isBuildable())
                return false;

            System.out.println("aaaaaaaaa");

            // コストを支払う
            // まず捨てるカード全部の参照を取得したい
            List<Card> cardsToDiscard = new ArrayList<>();
            // 建てるカードと被らないようにする
            List<Integer> modifiedOptions = options.stream().skip(1).distinct().filter(integer -> integer != num)
                    .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

            System.out.println("constructionlesser50" + modifiedOptions);
            // Display.myLog(modifiedOptions.toString());

            for (Integer integer : modifiedOptions) {
                cardsToDiscard.add(hands.get(integer));
            }

            // コストが足りているかチェック
            if (cardsToDiscard.size() < cost)
                return false;

            // 捨てる
            for (int i = 0; i < cost; i++) {
                player.discard(board, cardsToDiscard.get(i));
            }
            // 建てる
            player.build(cardToBuild);
            // draw
            player.draw(board);

            this.isWorked = true;
            return true;

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<Integer> promptChoice(Player player, Board board) {// TODO 拡張性のため残してある記述あり
        List<Integer> options = new ArrayList<>();
        System.out.println("建設するカードを選んでください");
        int option1 = Display.scanNextInt(player.getHands().size());
        options.add(option1);
        int cost = player.getHands().get(option1).getCost();
        System.out.println("捨てるカードを" + cost + "枚選んでください");
        for (int i = 0; i < cost; i++) {
            options.add(Display.scanNextInt(player.getHands().size()));
        }

        return options;
    }

}