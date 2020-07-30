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
    private int amounts;

    public Deck(int amounts) {
        this.amounts = amounts;
        // Init();
        randomInit();
    }

    public Card draw() {
        if (this.cards.size() == 0) {
            GameManager.getInstance().refreshDeck();
        }
        return this.cards.remove(new Random().nextInt(this.cards.size()));
    }

    private void randomInit() {

        while (this.cards.size() < this.amounts) {
            int id = new Random().nextInt(4);
            switch (id) {
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
                    System.out.println("invalid id");
            }

        }
    }

    private void Init() {

    }

    public void addCard(Card c) {
        this.cards.add(c);
    }

    // setter&getter
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
