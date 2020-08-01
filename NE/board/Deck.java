package NE.board;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import NE.card.Card;
import NE.card.agriculture.AgricultureCardA;
import NE.card.agriculture.AgricultureCardB;
import NE.card.agriculture.AgricultureCardC;
import NE.card.agriculture.AgricultureCardD;
import NE.card.construction.ConstructionCardB;
import NE.card.construction.ConstructionCardC;
import NE.card.construction.ConstructionCardD;
import NE.card.construction.ConstructionCardE;
import NE.card.facility.FacilityCardA;
import NE.card.facility.FacilityCardB;
import NE.card.facility.FacilityCardC;
import NE.card.facility.FacilityCardD;
import NE.card.facility.FacilityCardE;
import NE.card.facility.FacilityCardF;
import NE.card.facility.FacilityCardG;
import NE.card.facility.FacilityCardH;
import NE.card.facility.FacilityCardI;
import NE.card.industry.IndustryCardC;
import NE.card.industry.IndustryCardD;
import NE.card.industry.IndustryCardE;
import NE.card.industry.IndustryCardF;
import NE.card.industry.IndustryCardG;
import NE.card.industry.IndustryCardH;
import NE.card.market.MarketCardF;
import NE.card.market.MarketCardG;
import NE.card.market.MarketCardH;
import NE.card.market.MarketCardI;
import NE.main.GameManager;

public class Deck {
    private List<Card> cards = new LinkedList<>();
    private int initialAmounts;

    public Deck(int initialAmounts) {
        this.initialAmounts = initialAmounts;
        if (GameManager.getInstance().isRandomDeck()) {
            randomInit();
        } else {
            Init();
        }
    }

    private void randomInit() {
        while (this.cards.size() < this.initialAmounts) {
            int num = new Random().nextInt(4);
            switch (num) {
                case 0:
                    this.cards.add(new AgricultureCardD());
                    break;
                case 1:
                    this.cards.add(new ConstructionCardD());
                    break;
                case 2:
                    this.cards.add(new IndustryCardC());
                    break;
                case 3:
                    this.cards.add(new MarketCardH());
                    break;
                default:
                    System.out.println("invalid case");
            }
        }
    }

    private void Init() {
        for (int i = 0; i < 6; i++) {
            this.cards.add(new AgricultureCardA());
        }
        for (int i = 0; i < 4; i++) {
            this.cards.add(new AgricultureCardB());
        }
        for (int i = 0; i < 6; i++) {
            this.cards.add(new AgricultureCardC());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new AgricultureCardD());
        }
        for (int i = 0; i < 5; i++) {
            this.cards.add(new ConstructionCardB());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new ConstructionCardC());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new ConstructionCardD());
        }
        for (int i = 0; i < 3; i++) {
            this.cards.add(new ConstructionCardE());
        }
        for (int i = 0; i < 3; i++) {
            this.cards.add(new IndustryCardC());
        }
        for (int i = 0; i < 8; i++) {
            this.cards.add(new IndustryCardD());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new IndustryCardE());
        }
        for (int i = 0; i < 3; i++) {
            this.cards.add(new IndustryCardF());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new IndustryCardG());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new IndustryCardH());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new MarketCardF());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new MarketCardG());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new MarketCardH());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new MarketCardI());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new FacilityCardA());
        }
        for (int i = 0; i < 3; i++) {
            this.cards.add(new FacilityCardB());
        }
        for (int i = 0; i < 1; i++) {
            this.cards.add(new FacilityCardC());
        }
        for (int i = 0; i < 2; i++) {
            this.cards.add(new FacilityCardD());
        }
        for (int i = 0; i < 1; i++) {
            this.cards.add(new FacilityCardE());
        }
        for (int i = 0; i < 1; i++) {
            this.cards.add(new FacilityCardF());
        }
        for (int i = 0; i < 1; i++) {
            this.cards.add(new FacilityCardG());
        }
        for (int i = 0; i < 1; i++) {
            this.cards.add(new FacilityCardH());
        }
        for (int i = 0; i < 3; i++) {
            this.cards.add(new FacilityCardI());
        }

    }

    // error
    // private Card duplicateCard(Card original, int amounts) {
    // for (int i = 0; i < amounts; i++) {
    // Card newCard = original.getClass().getDeclaredConstructor().newInstance();
    // this.cards.add(newCard);
    // }
    // }

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
