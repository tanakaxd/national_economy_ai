package NE.card.construction;

import java.util.ArrayList;
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
        this.cost = 1;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.minHands = 1;
        this.amountsToBuild = 1;
    }

    @Override
    public boolean apply(Player player, Board board, List<Integer> options) {
        try {
            List<Card> hands = player.getHands();

            if (hands.size() < this.minHands || this.isWorked)
                return false;

            // 建てるカードを取得
            int num = (int) options.get(0);
            Card cardToBuild = hands.get(num);
            int cost = cardToBuild.getCost();

            if (cost > hands.size() - this.amountsToBuild || !cardToBuild.isBuildable())
                return false;

            // 建てるカードと捨てるカードがかぶらないように
            List<Integer> modifiedOptions = options.stream().distinct().skip(1).collect(Collectors.toList());

            if (!player.discard(board, modifiedOptions, cost))
                return false;

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
        Display.printChoices(player.getHands());
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