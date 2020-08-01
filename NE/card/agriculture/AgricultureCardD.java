package NE.card.agriculture;

import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.card.CommodityCard;
import NE.player.Player;

public class AgricultureCardD extends AgricultureCard {

    public AgricultureCardD() {
        this.id = 3;
        this.name = "醸造所";
        this.category = CardCategory.AGRICULTURE;
        this.cost = 4;
        this.value = 18;
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

        player.useWinery(true);
        this.isWorked = true;
        return true;
    }

}