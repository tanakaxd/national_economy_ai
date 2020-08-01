package NE.card.construction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class ConstructionCardD extends ConstructionCard {

    public ConstructionCardD() {
        this.id = 13;
        this.name = "プレハブ工務店";
        this.category = CardCategory.CONSTRUCTION;
        this.cost = 3;
        this.value = 12;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.minHands = 1;
        this.amountsToBuild = 1;
    }

    @Override
    public boolean apply(Player player, Board board) {
        List<Card> hands = player.getHands();

        if (hands.size() < this.minHands || this.isWorked)
            return false;

        Display.printChoices(hands);

        List<Integer> indexesToBuild = player.askBuild(board, this.amountsToBuild, this);
        int indexToBuild = indexesToBuild.get(0);

        int cost = hands.get(indexToBuild).getCost();
        if (cost + this.amountsToBuild > hands.size())
            return false;

        // 建てるカードの参照を取得
        List<Card> cardsToBuild = indexesToBuild.stream().map(index -> hands.get(index)).collect(Collectors.toList());

        // 全てのカードが建設可能かチェック。建てられないやつがある、またはcost10いかでないやつがある
        if (cardsToBuild.stream().anyMatch(c -> !c.isBuildable() || c.getValue() > 10))
            return false;

        // 建てる
        for (Card c : cardsToBuild) {
            player.build(c);
        }

        player.addVictoryPoint(1);
        this.isWorked = true;
        return true;

    }

}