package NE.card;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class CommodityCard extends Card {

    public CommodityCard() {
        this.id = 999;
        this.name = "消費財";
        this.category = CardCategory.COMMODITY;
        this.cost = 0;
        this.value = 0;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = false;
        this.isCommons = false;
        this.isWorked = false;

    }

    @Override
    public boolean apply(Player player, Board board) {
        return false;
    }

}