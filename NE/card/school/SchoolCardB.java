package NE.card.school;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class SchoolCardB extends SchoolCard {

    public SchoolCardB() {
        this.id = 41;
        this.name = "高等学校";
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
        if (this.isWorked || player.getWorkers().size() >= 4)
            return false;

        while (player.getWorkers().size() < 4) {
            player.addWorker(false);
        }
        this.isWorked = true;
        return true;
    }
}