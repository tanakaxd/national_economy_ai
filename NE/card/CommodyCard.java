package NE.card;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public class CommodyCard extends Card {

    public CommodyCard(int id, String name, int cost, int amountsInDeck) {
        super(id, name, cost, amountsInDeck);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean work(Player player, Board field, Deck deck) {
        // TODO Auto-generated method stub
        return false;
    }

}