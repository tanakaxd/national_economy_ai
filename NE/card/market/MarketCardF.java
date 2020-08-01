package NE.card.market;

public class MarketCardF extends MarketCard {

    public MarketCardF() {
        this.id = 35;
        this.category = CardCategory.MARKET;
        this.name = "食堂";
        this.cost = 1;
        this.value = 8;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;

        this.discards = 1;
        this.profit = 8;
    }

}