package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardB extends MarketCard {

    public MarketCardB() {
        this.id = 31;
        this.category = CardCategory.MARKET;
        this.name = "売店中";
        this.cost = 2;
        this.value = 12;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;

        this.discards = 1;
        this.profit = 8;
    }

}