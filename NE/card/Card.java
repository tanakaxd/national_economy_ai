package NE.card;

import java.util.ArrayList;
import java.util.List;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public abstract class Card {
    public enum CardCategory {
        CONSTRUCTION, AGRICULTURE, FACILITY, INDUSTRY, EDUCATION, COMMODITY
    }

    protected int id;
    protected CardCategory category;
    protected String name;
    protected int cost;
    protected boolean isWorked;
    protected int amountsInDeck;
    protected boolean isBuildable;

    public Card() {

    }

    public abstract boolean work(Player player, Board board, List<Integer> options);

    public abstract List<Integer> promptChoice(Player player, Board board);

    @Override
    public String toString() {
        // return "Card [cost=" + cost + ", isWorked=" + isWorked + ", name=" + name +
        // "]";
        String circle = isWorked ? "●" : "○";
        return String.format("[%s: cost=%d circle]", this.name, this.cost, circle);

        // StringBuilder sb = new StringBuilder();
        // sb.append("Card [cost=").append(cost)
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getAmountsInDeck() {
        return amountsInDeck;
    }

    public void setAmountsInDeck(int amountsInDeck) {
        this.amountsInDeck = amountsInDeck;
    }

    // public abstract Card clone();

    // public abstract boolean play();

}
