package NE.card.school;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class SchoolCardB extends SchoolCard {

    public SchoolCardB() {
        this.id = 41;
        this.category = CardCategory.EDUCATION;
        this.name = "高等学校";
        this.cost = 0;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked || player.getWorkers().size() >= 4)
            return false;

        while (player.getWorkers().size() < 4) {
            player.addWorker(false);
        }
        this.isWorked = true;
        return true;
    }
}