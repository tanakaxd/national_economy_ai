package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardH extends FacilityCard {

    public FacilityCardH() {
        this.id = 57;
        this.name = "投資銀行";
        this.category = CardCategory.FACILITY;
        this.cost = 6;
        this.value = 30;
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
        return player.getBuildings().stream().filter(c -> c.isFacility()).count() >= 4 ? 30 : 0;
    }

}