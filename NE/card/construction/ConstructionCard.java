package NE.card.construction;

import NE.card.Card;

public abstract class ConstructionCard extends Card {

    protected int minHands;
    protected int amountsToBuild;

    public ConstructionCard() {

    }

    public int getMinHands() {
        return minHands;
    }

    public int getAmountsToBuild() {
        return amountsToBuild;
    }

}