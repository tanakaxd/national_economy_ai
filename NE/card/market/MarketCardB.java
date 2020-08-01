package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardB extends MarketCard {

    public MarketCardB() {
        this.id = 31;
        this.category = CardCategory.MARKET;
        this.name = "市場";
        this.cost = 0;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.discards = 2;
        this.profit = 12;
    }

}