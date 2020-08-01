package NE.card.agriculture;

import NE.board.Board;
import NE.card.CommodityCard;
import NE.player.Player;

public class AgricultureCardB extends AgricultureCard {

    public AgricultureCardB() {
        this.id = 1;
        this.name = "菜園";
        this.category = CardCategory.AGRICULTURE;
        this.cost = 2;
        this.value = 10;
        this.description = "";
        this.isAgriculture = true;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;
        this.commodities = 2;
    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;

        for (int i = 0; i < this.commodities; i++) {
            player.getHands().add(new CommodityCard());
        }
        player.addVictoryPoint(1);
        this.isWorked = true;
        return true;
    }

}