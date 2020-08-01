package NE.card.industry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.main.GameManager;
import NE.player.Player;

public class IndustryCardB extends IndustryCard {

    public IndustryCardB() {
        this.id = 21;
        this.name = "採石場";
        this.category = CardCategory.INDUSTRY;
        this.cost = 0;
        this.value = 0;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = false;
        this.isCommons = true;
        this.isWorked = false;

        this.draws = 1;
        this.discards = 0;
    }

    // 特殊
    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;

        for (int i = 0; i < this.draws; i++) {
            player.draw(board);
        }
        GameManager.getInstance().setParentPlayer(player);

        this.setWorked(true);
        return true;
    }
}