package NE.card.industry;

import java.util.ArrayList;
import java.util.List;

import NE.board.Board;
import NE.display.Display;
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

    // 特殊
    @Override
    public boolean apply(Player player, Board board, List<Integer> options) {

        try {
            // player.discard(board, options, this.discards);
            player.draw(board);
            return true;

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

    }

    // 特殊
    @Override
    public List<Integer> promptChoice(Player player, Board board) {
        List<Integer> options = new ArrayList<>();
        return options;
    }

}