package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardA extends MarketCard {

    public MarketCardA() {
        this.id = 30;
        this.name = "露店";
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

        this.discards = 1;
        this.profit = 6;
    }

}