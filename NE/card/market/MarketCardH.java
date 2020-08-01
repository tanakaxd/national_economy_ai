package NE.card.market;

import java.util.List;

import NE.board.Board;
import NE.main.GameManager;
import NE.player.Player;

public class MarketCardH extends MarketCard {

    public MarketCardH() {
        this.id = 37;
        this.name = "観光牧場";
        this.category = CardCategory.MARKET;
        this.cost = 3;
        this.value = 14;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.discards = 0;
    }

    @Override
    public boolean apply(Player player, Board board) {
        int earnings = (int) (player.getHands().stream().filter(c -> c.getCategory() == CardCategory.COMMODITY).count()
                * 4);
        if (board.getGdp() < earnings || this.isWorked) {
            return false;
        }

        player.earnMoney(board, earnings);
        this.setWorked(true);
        return true;
    }

}