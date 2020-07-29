package NE.card.construction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class ConstructionLesser extends ConstructionCard {

    public ConstructionLesser() {
        this.id = 10;
        this.category = CardCategory.CONSTRUCTION;
        this.name = "建設小";
        this.isWorked = false;
        this.cost = 1;
        this.minHands = 1;
        this.isBuildable = true;

        this.amountsToBuild = 1;
    }

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {
        // template pattern を使う場合
        // 共通している処理は、最初と最後の処理
        // 最初のやつは、立てられるカードがない時等のreturn false
        // 最後のやつはコスト分のカードを捨てる処理
        try {
            List<Card> hands = player.getHands();

            if (hands.size() < this.minHands || this.isWorked)
                return false;

            // 建てるカードを取得
            int num = (int) options.get(0);
            Card cardToBuild = hands.get(num);
            int cost = cardToBuild.getCost();

            if (cost > hands.size() - this.amountsToBuild || !this.isBuildable)
                return false;

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