package NE.card.agriculture;

public class AgricultureCardA extends AgricultureCard {

    // public static int amountsInDeck = 4;

    public AgricultureCardA() {
        this.id = 0;
        this.category = CardCategory.AGRICULTURE;
        this.name = "芋畑";
        this.cost = 1;
        this.value = 6;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.commodities = 3;
    }

    // TODO override

}