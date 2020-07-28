package NE.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import NE.card.AgricultureCard;
import NE.card.Card;

public class Deck {
    private List<Card> cards = new LinkedList<>();
    // private List<Integer> cards = new LinkedList<>();

    private int amounts;

    public Deck(int amounts) {
        this.amounts = amounts;
        init();
    }

    public Card draw() {
        if (this.cards.size() == 0) {
            init();
        }
        // return CardDataBase.getCardForID(this.cards.get(new
        // Random().nextInt(this.cards.size())));
        return this.cards.remove(0);

    }

    private void init() {
        // for (int i = 0; i < CardDataBase.getCards().size(); i++) {
        // for (int j = 0; j < CardDataBase.getCards().get(i).getAmounts(); j++) {
        // this.cards.add(CardDataBase.getCards().get(i).getID());
        // }
        // }
        while (this.cards.size() < this.amounts) {
            int id = new Random().nextInt(2);
            switch (id) {
                case 0:
                    this.cards.add(new AgricultureCard(0, "a", 1, 3));
                    break;
                case 1:
                    this.cards.add(new AgricultureCard(1, "b", 3, 3));
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
