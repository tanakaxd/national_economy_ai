package NE.board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import NE.card.Card;
import NE.card.Card.CardCategory;
import NE.card.construction.ConstructionLesser;
import NE.card.industry.IndustryLesser;
import NE.card.market.MarketLesser;
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
        this.buildings.add(new ConstructionLesser());
        this.buildings.add(new ConstructionLesser());
        this.buildings.add(new SchoolLesser());
        this.buildings.add(new MarketLesser());
    }

    public void refreshDeck() {
        System.out.println("deck refreshed!");
        List<Card> filteredTrash = this.trash.stream().filter(c -> c.getCategory() != CardCategory.COMMODITY)
                .collect(Collectors.toList());
        while (filteredTrash.size() > 0) {
            this.deck.addCard(filteredTrash.remove(0));
        }
    }

    public Card draw() {
        if (this.deck.getDeckSize() == 0) {
            refreshDeck();
        }
        return this.deck.draw();
    }

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

    public void addGdp(int amount) {
        this.gdp += amount;
    }

    public void unbanAll() {
        for (Card card : buildings) {
            card.setWorked(false);
        }
    }

    // #region setter&getter

    public List<Card> getBuildings() {
        return buildings;
    }

    public int getGdp() {
        return gdp;
    }

    public void setGdp(int gdp) {
        this.gdp = gdp;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Card> getTrash() {
        return trash;
    }
    // #endregion

}
