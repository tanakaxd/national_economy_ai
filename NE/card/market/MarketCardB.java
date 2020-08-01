package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardB extends MarketCard {

    public MarketCardB() {
        this.id = 31;
        this.name = "市場";
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

        this.discards = 2;
        this.profit = 12;
    }

}