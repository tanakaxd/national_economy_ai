package NE.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import NE.card.Card;
import NE.card.agriculture.AgricultureCard;
import NE.card.agriculture.AgricultureLesser;
import NE.card.construction.ConstructionLesser;
import NE.card.industry.IndustryLesser;

public class Deck {
    private List<Card> cards = new LinkedList<>();
    private int amounts;

    public Deck(int amounts) {
        this.amounts = amounts;
        init();
    }

    public Card draw() {
        if (this.cards.size() == 0) {
            init();
        }
        return this.cards.remove(0);
    }

    private void init() {

        while (this.cards.size() < this.amounts) {
            int id = new Random().nextInt(3);
            switch (id) {
                case 0:
                    this.cards.add(new AgricultureLesser());
                    break;
                case 1:
                    this.cards.add(new ConstructionLesser());
                    break;
                case 2:
                    this.cards.add(new IndustryLesser());
                    break;

                default:
                    System.out.println("invalid id");
            }

        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
