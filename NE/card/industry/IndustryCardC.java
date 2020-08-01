package NE.card.industry;

public class IndustryCardC extends IndustryCard {

    public IndustryCardC() {
        this.id = 22;
        this.category = CardCategory.INDUSTRY;
        this.name = "食品工場";
        this.cost = 2;
        this.value = 12;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;

        this.draws = 4;
        this.discards = 2;
    }

    // TODO override

}