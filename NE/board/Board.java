package NE.board;

import java.util.ArrayList;
import java.util.List;

import NE.card.Card;
import NE.card.agriculture.AgricultureLesser;
import NE.card.construction.ConstructionLesser;
import NE.card.industry.IndustryLesser;
import NE.card.school.SchoolLesser;

public class Board {
    private Deck deck;
    private List<Card> buildings = new ArrayList<>();
    private List<Card> trash = new ArrayList<>();
    private int gdp = 0;

    public Board(int cardsInDeck) {
        this.deck = new Deck(cardsInDeck);
        this.buildings.add(new IndustryLesser());
        this.buildings.add(new ConstructionLesser());
        this.buildings.add(new SchoolLesser());
        this.buildings.add(new AgricultureLesser());

    }

    // public void workOnCard(int option, Deck deck, Player player) {
    // this.buildings.get(option).work(player, this, deck);
    // }

    public void ban() {
        for (int i = this.buildings.size() - 1; i >= 0; i--) {
            if (this.buildings.get(i).isWorked()) {
                continue;
            } else {
                this.buildings.get(i).setWorked(true);
                break;
            }
        }
    }

    public List<Card> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Card> buildings) {
        this.buildings = buildings;
    }

    public void addGdp(int amount) {
        this.gdp += amount;
    }

    public int getGdp() {
        return gdp;
    }

    public void setGdp(int gdp) {
        this.gdp = gdp;
    }

    public void unbanAll() {
        for (Card card : buildings) {
            card.setWorked(false);
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public List<Card> getTrash() {
        return trash;
    }

    public void setTrash(List<Card> trash) {
        this.trash = trash;
    }

}
