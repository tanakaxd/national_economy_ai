package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardD extends MarketCard {

    public MarketCardD() {
        this.id = 33;
        this.category = CardCategory.MARKET;
        this.name = "百貨店";
        this.cost = 0;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.discards = 4;
        this.profit = 24;
    }

}