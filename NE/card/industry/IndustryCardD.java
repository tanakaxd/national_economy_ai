package NE.card.industry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class IndustryCardD extends IndustryCard {
    public IndustryCardD() {
        this.id = 23;
        this.name = "食品工場";
        this.category = CardCategory.INDUSTRY;
        this.cost = 2;
        this.value = 12;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = true;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.draws = 4;
        this.discards = 2;
    }

    @Override
    public int getCost(Player player) {
        return player.getBuildings().stream().anyMatch(c -> c.isAgriculture()) ? this.cost - 1 : this.cost;
    }
}