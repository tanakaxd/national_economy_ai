package NE.card.agriculture;

import java.util.ArrayList;
import java.util.List;

import NE.board.Board;
import NE.board.Deck;
import NE.card.Card;
import NE.card.CommodityCard;
import NE.player.Player;

public abstract class AgricultureCard extends Card {

    protected int commodities;

    public AgricultureCard() {

    }

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {
        if (this.isWorked)
            return false;
        for (int i = 0; i < this.commodities; i++) {
            player.getHands().add(new CommodityCard());
        }
        this.isWorked = true;
        return true;
    }

    // protected abstract boolean doWork(Player player, Board board, List<Integer>
    // options);

    @Override
    public List<Integer> promptChoice(Player player, Board board) {
        return null;
    }

    public int getCommodities() {
        return commodities;
    }

    public void setCommodities(int commodities) {
        this.commodities = commodities;
    }

}