package NE.card;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public abstract class Card {
    public enum CardCategory {
        CONSTRUCTION, AGRICULTURE, FACILITY, INDUSTRY, EDUCATION, COMMODITY, MARKET
    }

    protected int id;
    protected CardCategory category;
    protected boolean isAgriculture;
    protected boolean isFactory;
    protected boolean isFacility;
    protected String name;
    protected String description;
    protected int cost;
    protected int value;
    // protected int amountsInDeck;
    protected boolean isWorked;
    protected boolean isBuildable;
    protected boolean isCommons;

    public Card() {

    }

    public abstract boolean apply(Player player, Board board, List<Integer> options);

    public abstract List<Integer> promptChoice(Player player, Board board);

    @Override
    public String toString() {
        // TODO
        String circle = isWorked ? "●" : "○";
        String s = String.format("[%d %s $%d %s]", this.cost, this.name, this.value, circle);
        return s;
    }

    // #region sg

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isWorked() {
        return isWorked;
    }

    public void setWorked(boolean isWorked) {
        this.isWorked = isWorked;
    }

    public CardCategory getCategory() {
        return category;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isBuildable() {
        return isBuildable;
    }

    public void setBuildable(boolean isBuildable) {
        this.isBuildable = isBuildable;
    }

    // #endregion

}
