package NE.card.facility;

import NE.board.Board;
import NE.player.Player;

public class FacilityCardI extends FacilityCard {

    public FacilityCardI() {
        this.id = 58;
        this.name = "大聖堂";
        this.category = CardCategory.FACILITY;
        this.cost = 10;
        this.value = 50;
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

    @Override
    public int getCost(Player player) {
        return player.getVictoryPoint() >= 5 ? this.cost - 4 : this.cost;
    }

}