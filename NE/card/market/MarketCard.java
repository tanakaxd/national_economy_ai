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
    public boolean apply(Player player, Board board) {
        if (board.getHoldholdIncome() < this.profit || player.getHands().size() < this.discards || this.isWorked) {
            return false;
        }

        List<Integer> indexesToDiscard = player.askDiscard(board, this.discards);
        if (indexesToDiscard.size() < this.discards)
            return false;

        for (Integer integer : indexesToDiscard) {
            player.discard(board, integer);
        }

        player.earnMoney(board, this.profit);

        if (this.isCommons && GameManager.getInstance().hasManyPlayers()) {

        } else {
            this.setWorked(true);
        }
        return true;
    }

}