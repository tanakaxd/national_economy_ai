package NE.card;

import java.util.List;

import NE.board.Board;
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
    public boolean apply(Player player, Board board) {
        // TODO Auto-generated method stub
        return false;
    }

}