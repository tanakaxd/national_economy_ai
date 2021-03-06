package NE.card.industry;

import NE.board.Board;
import NE.main.GameManager;
import NE.player.Player;

public class IndustryCardC extends IndustryCard {

    public IndustryCardC() {
        this.id = 22;
        this.name = "鉄工所";
        this.category = CardCategory.INDUSTRY;
        this.cost = 1;
        this.value = 8;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = true;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.draws = 1;
        this.discards = 0;
    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;
        int amounts = player.getUseMine() ? 2 : 1;
        for (int i = 0; i < amounts; i++) {
            player.draw(board);
        }
        this.setWorked(true);
        return true;
    }

}