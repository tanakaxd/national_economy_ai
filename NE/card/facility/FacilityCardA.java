package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardA extends FacilityCard {

    public FacilityCardA() {
        this.id = 50;
        this.name = "墓地";
        this.category = CardCategory.FACILITY;
        this.cost = 1;
        this.value = 8;
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
        return player.getHands().isEmpty() ? 8 : 0;
    }

}