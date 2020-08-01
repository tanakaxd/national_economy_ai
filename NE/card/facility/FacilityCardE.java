package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardE extends FacilityCard {

    public FacilityCardE() {
        this.id = 54;
        this.name = "植物園";
        this.category = CardCategory.FACILITY;
        this.cost = 4;
        this.value = 22;
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
        return player.getBuildings().stream().filter(c -> c.isAgriculture()).count() >= 3 ? 22 : 0;
    }

}