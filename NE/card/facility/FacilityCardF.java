package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardF extends FacilityCard {

    public FacilityCardF() {
        this.id = 55;
        this.name = "輸出港";
        this.category = CardCategory.FACILITY;
        this.cost = 5;
        this.value = 24;
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
        return player.getBuildings().stream().filter(c -> c.isFactory()).count() >= 2 ? 24 : 0;
    }

}