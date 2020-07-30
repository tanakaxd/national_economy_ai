package NE.card.agriculture;

public class AgricultureLesser extends AgricultureCard {

    // public static int amountsInDeck = 4;

    public AgricultureLesser() {
        this.id = 0;
        this.category = CardCategory.AGRICULTURE;
        this.name = "農業小";
        this.cost = 1;
        this.value = 6;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.commodities = 2;
    }

}