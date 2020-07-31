package NE.card.construction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class ConstructionCardE extends ConstructionCard {

    public ConstructionCardE() {
        this.id = 14;
        this.category = CardCategory.CONSTRUCTION;
        this.name = "宇宙建設";
        this.cost = 4;
        this.value = 15;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;

        this.minHands = 2;
        this.amountsToBuild = 2;
    }

    @Override
    public boolean apply(Player player, Board board) {
        // List<Card> hands = player.getHands();

        // if (hands.size() < this.minHands || this.isWorked)
        // return false;

        // Display.printChoices(player.getHands());
        // System.out.println("建設するカードを選んでください");
        // List<Integer> indexesToBuild = player.askBuild(board);
        // int cost = player.getHands().get(option1).getCost();
        // System.out.println("捨てるカードを" + cost + "枚選んでください");

        // List<Integer> indexesToDiscard = player.askDiscard(board, cost);

        // // 建てるカードを取得
        // List<Card> cardsToBuild = indexesToBuild.stream().map(index ->
        // hands.get(index)).collect(Collectors.toList());

        // if (cost > hands.size() - this.amountsToBuild ||
        // cardsToBuild.stream().allMatch(c -> c.isBuildable()))
        // return false;

        // // 建てるカードと捨てるカードがかぶらないように
        // List<Integer> modifiedOptions =
        // options.stream().distinct().skip(1).collect(Collectors.toList());

        // if (!player.discard(board, modifiedOptions, cost))
        // return false;

        // // 建てる
        // player.build(cardToBuild);
        // this.isWorked = true;
        return true;

    }
}