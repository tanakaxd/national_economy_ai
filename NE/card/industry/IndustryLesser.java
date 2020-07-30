package NE.card.industry;

import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.player.Player;

public class IndustryLesser extends IndustryCard {

    public IndustryLesser() {
        this.id = 20;
        this.category = CardCategory.INDUSTRY;
        this.name = "工場小";
        this.cost = 1;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.draws = 1;
        this.discards = 0;
    }

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {

        try {
            List<Card> hands = player.getHands();

            if (hands.size() < this.discards || this.isWorked)
                return false;

            for (int i = 0; i < this.discards; i++) {
                player.discard(board, options.get(i));
            }

            for (int i = 0; i < this.draws; i++) {
                player.draw(board);
            }
            return true;

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

    }

}