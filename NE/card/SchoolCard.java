package NE.card;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public class SchoolCard extends Card {

    public SchoolCard(int id, String name, int cost, int amountsInDeck) {
        super(id, name, cost, amountsInDeck);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean work(Player player, Board field, Deck deck) {
        // TODO Auto-generated method stub
        player.setWorkersCount(player.getWorkersCount() + 1);
        this.isWorked = true;

        return false;
    }

}