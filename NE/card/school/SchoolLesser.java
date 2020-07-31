package NE.card.school;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class SchoolLesser extends SchoolCard {

    public SchoolLesser() {
        this.id = 40;
        this.category = CardCategory.EDUCATION;
        this.name = "学校小";
        this.isWorked = false;
        this.cost = 0;
        this.isBuildable = true;

    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;

        boolean success = player.addWorker(false);
        if (!success)
            return false;

        this.isWorked = true;
        return true;
    }
}