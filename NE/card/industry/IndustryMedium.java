package NE.card.industry;

import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.player.Player;

public class IndustryMedium extends IndustryCard {

    public IndustryMedium() {
        this.id = 21;
        this.category = CardCategory.INDUSTRY;
        this.name = "工場中";
        this.cost = 2;
        this.value = 12;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;

        this.draws = 4;
        this.discards = 2;
    }

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {

        if (this.isWorked)
            return false;

        try {
            List<Card> hands = player.getHands();

            if (hands.size() < this.discards || this.isWorked)
                return false;

            for (int i = 0; i < this.discards; i++) {
                player.discard(board, options.get(i));// TODO outofbounds
            }

            for (int i = 0; i < this.draws; i++) {
                player.draw(board);
            }
            this.isWorked = true;
            return true;

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

    }

}