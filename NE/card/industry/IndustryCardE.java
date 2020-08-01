package NE.card.industry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class IndustryCardE extends IndustryCard {
    public IndustryCardE() {
        this.id = 24;
        this.name = "研究所";
        this.category = CardCategory.INDUSTRY;
        this.cost = 3;
        this.value = 16;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.draws = 2;
        this.discards = 0;
    }

    @Override
    public boolean apply(Player player, Board board) {
        List<Card> hands = player.getHands();

        if (hands.size() < this.discards || this.isWorked)
            return false;

        // draw
        for (int i = 0; i < this.draws; i++) {
            player.draw(board);
        }

        player.addVictoryPoint(1);

        this.isWorked = true;
        return true;
    }
}