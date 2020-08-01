package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardG extends FacilityCard {

    public FacilityCardG() {
        this.id = 56;
        this.name = "博物館";
        this.category = CardCategory.FACILITY;
        this.cost = 5;
        this.value = 34;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = true;
        this.isBuildable = true;
        this.isCommons = false;
    }

    @Override
    public boolean apply(Player player, Board board) {
        return false;
    }

    @Override
    public int calcBonus(Player player) {
        return 0;
    }

}