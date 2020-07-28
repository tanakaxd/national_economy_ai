package NE.card;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public class DrawCard extends Card {

    public DrawCard(int id, String name, int cost, int amountsInDeck) {
        super(id, name, cost, amountsInDeck);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean work(Player player, Board field, Deck deck) {
        // TODO Auto-generated method stub
        player.getHands().add(deck.draw());
        this.isWorked = true;

        return false;
    }

}