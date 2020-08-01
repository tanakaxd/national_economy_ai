package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.main.GameManager;
import NE.player.Player;

public class MarketCardG extends MarketCard {

    public MarketCardG() {
        this.id = 36;
        this.name = "宝くじ";
        this.category = CardCategory.MARKET;
        this.cost = 2;
        this.value = 10;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.discards = 0;
        this.profit = 10;
    }

    @Override
    public boolean apply(Player player, Board board) {
        if (board.getGdp() < 20 || this.isWorked) {
            return false;
        }

        player.earnMoney(board, this.profit);
        this.setWorked(true);
        return true;
    }

}