package NE.card.market;

import java.util.ArrayList;
import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public abstract class MarketCard extends Card {

    protected int discards;
    protected int profit;

    @Override
    public boolean apply(Player player, Board board, List<Integer> options) {
        if (board.getGdp() < this.profit || player.getHands().size() < this.discards || this.isWorked) {
            return false;
        }

        player.discard(board, options, this.discards);

        player.earnMoney(board, this.profit);

        this.setWorked(true);
        return true;
    }

    @Override
    public List<Integer> promptChoice(Player player, Board board) {
        System.out.println("捨てるカードを選べ");
        Display.printChoices(player.getHands());
        List<Integer> options = new ArrayList<>();
        for (int i = 0; i < this.discards; i++) {
            options.add(Display.scanNextInt(player.getHands().size()));
        }
        return options;
    }
}