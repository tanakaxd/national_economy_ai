package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardC extends MarketCard {

    public MarketCardC() {
        this.id = 32;
        this.category = CardCategory.MARKET;
        this.name = "スーパー";
        this.cost = 0;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.discards = 3;
        this.profit = 18;
    }

}