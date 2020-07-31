package NE.card.agriculture;

import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.card.CommodityCard;
import NE.player.Player;

public abstract class AgricultureCard extends Card {

    protected int commodities;

    public AgricultureCard() {

    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;
        for (int i = 0; i < this.commodities; i++) {
            player.getHands().add(new CommodityCard());
        }
        this.isWorked = true;
        return true;
    }

    public int getCommodities() {
        return commodities;
    }

    public void setCommodities(int commodities) {
        this.commodities = commodities;
    }

}