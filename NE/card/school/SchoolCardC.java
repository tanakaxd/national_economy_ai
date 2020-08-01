package NE.card.school;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public class SchoolCardC extends SchoolCard {

    public SchoolCardC() {
        this.id = 42;
        this.name = "大学";
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
        if (this.isWorked || player.getWorkers().size() >= 5)
            return false;

        while (player.getWorkers().size() < 5) {
            player.addWorker(false);
        }
        this.isWorked = true;
        return true;
    }
}