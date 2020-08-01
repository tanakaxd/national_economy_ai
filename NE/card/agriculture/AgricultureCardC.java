package NE.card.agriculture;

import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.card.CommodityCard;
import NE.player.Player;

public class AgricultureCardC extends AgricultureCard {

    public AgricultureCardC() {
        this.id = 2;
        this.name = "養殖場";
        this.category = CardCategory.AGRICULTURE;
        this.cost = 2;
        this.value = 12;
        this.description = "";
        this.isAgriculture = true;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;
    }

    @Override
    public boolean apply(Player player, Board board) {
        if (this.isWorked)
            return false;

        List<Card> hands = player.getHands();
        int amounts;
        if (hands.stream().anyMatch(c -> c.getCategory() == CardCategory.COMMODITY)) {
            amounts = 3;
        } else {
            amounts = 2;
        }
        for (int i = 0; i < amounts; i++) {
            player.getHands().add(new CommodityCard());
        }
        this.isWorked = true;
        return true;
    }

}