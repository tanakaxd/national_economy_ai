package NE.card.industry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class IndustryCardF extends IndustryCard {
    public IndustryCardF() {
        this.id = 25;
        this.name = "造船所";
        this.category = CardCategory.INDUSTRY;
        this.cost = 4;
        this.value = 20;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = true;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.draws = 6;
        this.discards = 3;
    }

}