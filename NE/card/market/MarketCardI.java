package NE.card.market;

public class MarketCardI extends MarketCard {

    public MarketCardI() {
        this.id = 38;
        this.name = "遊園地";
        this.category = CardCategory.MARKET;
        this.cost = 5;
        this.value = 24;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.discards = 2;
        this.profit = 25;
    }

}