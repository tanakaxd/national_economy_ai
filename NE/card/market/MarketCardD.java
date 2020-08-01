package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class MarketCardD extends MarketCard {

    public MarketCardD() {
        this.id = 33;
        this.name = "百貨店";
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

        this.discards = 4;
        this.profit = 24;
    }

}