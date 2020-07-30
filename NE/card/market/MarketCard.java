package NE.card.market;

import java.util.ArrayList;
import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.main.GameManager;
import NE.player.Player;

public abstract class MarketCard extends Card {

    protected int discards;
    protected int profit;

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {
        if (board.getGdp() < this.profit || player.getHands().size() < this.discards) {
            return false;
        }

        for (int i = 0; i < this.discards; i++) {
            player.discard(board, options.get(i));
        }

        player.earnMoney(board, this.profit);

        this.setWorked(true);
        return true;
    }

    @Override
    public List<Integer> promptChoice(Player player, Board board) {
        List<Integer> options = new ArrayList<>();
        for (int i = 0; i < this.discards; i++) {
            options.add(Display.scanNextInt(player.getHands().size()));
        }
        return options;
    }
}