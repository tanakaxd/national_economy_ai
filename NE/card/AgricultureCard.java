package NE.card;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public class AgricultureCard extends Card {
    {
        this.commodities = 1;
    }

    int commodities;

    public AgricultureCard() {
        super();
    }

    public AgricultureCard(int id, String name, int cost, int amountsInDeck) {
        super(id, name, cost, amountsInDeck);
    }

    @Override
    public boolean work(Player player, Board field, Deck deck) {
        // TODO Auto-generated method stub
        for (int i = 0; i < this.commodities; i++) {

            player.getHands().add(new CommodyCard(100, "commodity", 100, 0));
        }

        this.isWorked = true;

        return true;
    }

}