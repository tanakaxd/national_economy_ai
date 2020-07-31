package NE.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import NE.card.Card;
import NE.card.agriculture.AgricultureLesser;
import NE.card.construction.ConstructionMedium;
import NE.card.industry.IndustryMedium;
import NE.card.market.MarketMedium;
import NE.main.GameManager;

public class Deck {
    private List<Card> cards = new LinkedList<>();
    private int initialAmounts;

    public Deck(int initialAmounts) {
        this.initialAmounts = initialAmounts;
        if (GameManager.getInstance().isRandomDeck()) {
            randomInit();
        } else {
            // Init();
        }
    }

    private void randomInit() {
        while (this.cards.size() < this.initialAmounts) {
            int num = new Random().nextInt(4);
            switch (num) {
                case 0:
                    this.cards.add(new AgricultureLesser());
                    break;
                case 1:
                    this.cards.add(new ConstructionMedium());
                    break;
                case 2:
                    this.cards.add(new IndustryMedium());
                    break;
                case 3:
                    this.cards.add(new MarketMedium());
                    break;
                default:
                    System.out.println("invalid case");
            }
        }
    }

    private void Init() {

    }

    public Card draw() {
        return this.cards.remove(new Random().nextInt(this.cards.size()));
    }

    public void addCard(Card c) {
        this.cards.add(c);
    }

    // #region sg
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getDeckSize() {
        return this.cards.size();
    }
    // #endregion

}
