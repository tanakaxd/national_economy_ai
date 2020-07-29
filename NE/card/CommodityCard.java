package NE.card;

import java.util.ArrayList;
import java.util.List;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public class CommodityCard extends Card {

    public CommodityCard() {
        this.id = 999;
        this.category = CardCategory.COMMODITY;
        this.name = "消費財";
        this.isWorked = false;
        this.cost = 0;
        this.isBuildable = false;

    }

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Integer> promptChoice(Player player, Board board) {
        // TODO Auto-generated method stub
        return null;
    }

}