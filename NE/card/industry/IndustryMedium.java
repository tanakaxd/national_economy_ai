package NE.card.industry;

public class IndustryMedium extends IndustryCard {

    public IndustryMedium() {
        this.id = 21;
        this.category = CardCategory.INDUSTRY;
        this.name = "工場中";
        this.cost = 2;
        this.value = 12;
        this.isWorked = false;
        this.isBuildable = true;
        this.isCommons = false;

        this.draws = 4;
        this.discards = 2;
    }

}