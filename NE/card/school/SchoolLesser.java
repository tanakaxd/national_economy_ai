package NE.card.school;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class SchoolLesser extends SchoolCard {

    public SchoolLesser() {
        this.id = 30;
        this.category = CardCategory.EDUCATION;
        this.name = "学校小";
        this.isWorked = false;
        this.cost = 1;
        this.isBuildable = true;

    }

    @Override
    public boolean work(Player player, Board board, List<Integer> options) {
        if (this.isWorked)
            return false;

        boolean success = player.addWorker();
        if (!success)
            return false;

        this.isWorked = true;
        return false;
    }

    @Override
    public List<Integer> promptChoice(Player player, Board board) {
        return null;
    }

}