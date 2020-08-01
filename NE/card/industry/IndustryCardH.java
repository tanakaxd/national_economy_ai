package NE.card.industry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class IndustryCardH extends IndustryCard {
    public IndustryCardH() {
        this.id = 27;
        this.name = "石油コンビナート";
        this.category = CardCategory.INDUSTRY;
        this.cost = 6;
        this.value = 28;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = true;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.draws = 4;
        this.discards = 0;
    }

}