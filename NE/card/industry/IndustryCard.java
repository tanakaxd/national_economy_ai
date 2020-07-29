package NE.card.industry;

import java.util.ArrayList;
import java.util.List;

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

            this.isWorked = true;
            return true;

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<Integer> promptChoice(Player player, Board board) {
        List<Integer> options = new ArrayList<>();
        System.out.println("捨てるカードを" + this.discards + "枚選んでください");
        for (int i = 0; i < this.discards; i++) {
            options.add(Display.scanNextInt(player.getHands().size()));
        }
        return options;
    }

}