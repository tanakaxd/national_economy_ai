package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardC extends FacilityCard {

    public FacilityCardC() {
        this.id = 52;
        this.name = "会計事務所";
        this.category = CardCategory.FACILITY;
        this.cost = 3;
        this.value = 12;
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
        return player.getVictoryPoint();
    }

}