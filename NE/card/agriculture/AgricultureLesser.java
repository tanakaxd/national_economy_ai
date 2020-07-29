package NE.card.agriculture;

public class AgricultureLesser extends AgricultureCard {

    // public static int amountsInDeck = 4;

    public AgricultureLesser() {
        this.id = 0;
        this.category = CardCategory.AGRICULTURE;
        this.name = "農業小";
        this.isWorked = false;
        this.cost = 1;
        this.isBuildable = true;
        this.commodities = 2;
    }

}