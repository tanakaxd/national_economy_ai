package NE.card.industry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class IndustryCardG extends IndustryCard {
    public IndustryCardG() {
        this.id = 26;
        this.name = "工業団地";
        this.category = CardCategory.INDUSTRY;
        this.cost = 5;
        this.value = 22;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = true;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.draws = 3;
        this.discards = 0;
    }

    @Override
    public int getCost(Player player) {

        return (int) (this.cost - player.getBuildings().stream().filter(c -> c.isFactory()).count());
    }

}