package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardE extends MarketCard {

    public MarketCardE() {
        this.id = 34;
        this.name = "万博";
        this.category = CardCategory.MARKET;
        this.cost = 0;
        this.value = 0;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = false;
        this.isCommons = true;
        this.isWorked = false;

        this.discards = 5;
        this.profit = 30;
    }

}