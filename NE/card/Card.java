package NE.card;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public abstract class Card {
    protected int id;
    protected String name;
    protected int cost;
    protected boolean isWorked;
    protected int amountsInDeck;
    // private boolean isBuildable;

    public Card(int id, String name, int cost, int amountsInDeck) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.amountsInDeck = amountsInDeck;
    }

    public Card() {
        this.id = 0;
        this.name = "default";
        this.cost = 0;
        this.amountsInDeck = 0;

    }

    public abstract boolean work(Player player, Board board);

    @Override
    public String toString() {
        return "Card [cost=" + cost + ", isWorked=" + isWorked + ", name=" + name + "]";
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
