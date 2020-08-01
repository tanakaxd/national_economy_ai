package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardD extends FacilityCard {

    public FacilityCardD() {
        this.id = 53;
        this.name = "鉄道駅";
        this.category = CardCategory.FACILITY;
        this.cost = 3;
        this.value = 18;
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
        return player.getBuildings().size() >= 6 ? 18 : 0;
    }

}