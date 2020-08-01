package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardA extends MarketCard {

    public MarketCardA() {
        this.id = 30;
        this.category = CardCategory.MARKET;
        this.name = "売店小";
        this.cost = 0;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.discards = 1;
        this.profit = 6;
    }

}