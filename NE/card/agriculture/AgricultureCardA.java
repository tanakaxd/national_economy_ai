package NE.card.agriculture;

import NE.board.Board;
import NE.card.CommodityCard;
import NE.player.Player;

public class AgricultureCardA extends AgricultureCard {

    // public static int amountsInDeck = 4;

    public AgricultureCardA() {
        this.id = 0;
        this.name = "芋畑";
        this.category = CardCategory.AGRICULTURE;
        this.cost = 1;
        this.value = 6;
        this.description = "";
        this.isAgriculture = true;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;
    }

    protected int id;
    protected String name;
    protected CardCategory category;
    protected int cost;
    protected int value;
    protected String description;
    // protected int amountsInDeck;
    protected boolean isAgriculture;
    protected boolean isFactory;
    protected boolean isFacility;
    protected boolean isWorked;
    protected boolean isBuildable;
    protected boolean isCommons;

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;

        while (player.getHands().size() < 3) {
            player.getHands().add(new CommodityCard());
        }
        this.isWorked = true;
        return true;
    }

}