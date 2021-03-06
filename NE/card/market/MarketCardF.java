package NE.card.market;

public class MarketCardF extends MarketCard {

    public MarketCardF() {
        this.id = 35;
        this.name = "食堂";
        this.category = CardCategory.MARKET;
        this.cost = 1;
        this.value = 8;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.discards = 1;
        this.profit = 8;
    }

}