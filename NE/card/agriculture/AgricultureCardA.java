package NE.card.agriculture;

import NE.board.Board;
import NE.card.CommodityCard;
import NE.player.Player;

public class AgricultureCardA extends AgricultureCard {

    // public static int amountsInDeck = 4;

    public AgricultureCardA() {
        this.id = 0;
        this.name = "芋畑";
        this.category = CardCategory.AGRICULTURE;
        this.cost = 1;
        this.value = 6;
        this.description = "";
        this.isAgriculture = true;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;
    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;

        while (player.getHands().size() < 3) {
            player.getHands().add(new CommodityCard());
        }
        this.isWorked = true;
        return true;
    }

}