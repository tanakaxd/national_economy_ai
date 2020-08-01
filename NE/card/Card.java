package NE.card;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public abstract class Card {
    public enum CardCategory {
        CONSTRUCTION, AGRICULTURE, FACILITY, INDUSTRY, EDUCATION, COMMODITY, MARKET
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

    public Card() {
        this.isWorked = false;
    }

    public abstract boolean apply(Player player, Board board);

    // public abstract List<Integer> promptChoice(Player player, Board board);

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

    // TODO コストはプレイヤーの所有物件、手札、勝利ポイントによって変動する
    // public int getCost(Player player) {
    // return cost;
    // }
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

    public String getDescription() {
        return description;
    }

    public boolean isAgriculture() {
        return isAgriculture;
    }

    public boolean isFactory() {
        return isFactory;
    }

    public boolean isFacility() {
        return isFacility;
    }

    public boolean isCommons() {
        return isCommons;
    }

    // #endregion

}
