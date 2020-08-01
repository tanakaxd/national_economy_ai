package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardC extends MarketCard {

    public MarketCardC() {
        this.id = 32;
        this.name = "スーパーマーケット";
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

        this.discards = 3;
        this.profit = 18;
    }

}