package NE.card.industry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public abstract class IndustryCard extends Card {

    protected int draws;
    protected int discards;

    public IndustryCard() {

    }

    @Override
    public boolean apply(Player player, Board board) {
        List<Card> hands = player.getHands();

        if (hands.size() < this.discards || this.isWorked)
            return false;

        Display.printChoices(hands);

        List<Integer> indexesToDiscard = player.askDiscard(board, cost).stream().distinct()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        if (indexesToDiscard.size() < this.discards)
            return false;

        // 捨てる
        for (Integer integer : indexesToDiscard) {
            player.discard(board, integer);
        }

        // draw
        for (int i = 0; i < this.draws; i++) {
            player.draw(board);
        }

        this.isWorked = true;
        return true;
    }

}