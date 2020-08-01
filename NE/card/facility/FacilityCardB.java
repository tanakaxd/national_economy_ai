package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardB extends FacilityCard {

    public FacilityCardB() {
        this.id = 51;
        this.name = "旧市街";
        this.category = CardCategory.FACILITY;
        this.cost = 2;
        this.value = 10;
        this.description = "";
        this.isAgriculture = true;
        this.isFactory = true;
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