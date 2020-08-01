package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardE extends MarketCard {

    public MarketCardE() {
        this.id = 34;
        this.category = CardCategory.MARKET;
        this.name = "万博";
        this.cost = 0;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.discards = 5;
        this.profit = 30;
    }

}