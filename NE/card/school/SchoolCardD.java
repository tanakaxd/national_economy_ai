package NE.card.school;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class SchoolCardD extends SchoolCard {

    public SchoolCardD() {
        this.id = 43;
        this.name = "専門学校";
        this.category = CardCategory.EDUCATION;
        this.cost = 0;
        this.value = 0;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = false;
        this.isCommons = true;

    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;

        boolean success = player.addWorker(true);
        if (!success)
            return false;

        this.isWorked = true;
        return true;
    }
}