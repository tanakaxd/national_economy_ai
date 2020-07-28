package NE.card;

import NE.board.Board;
import NE.board.Deck;
import NE.player.Player;

public class BuilderCard extends Card {

    public BuilderCard(int id, String name, int cost, int amountsInDeck) {
        super(id, name, cost, amountsInDeck);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean work(Player player, Board field, Deck deck) {
        // 建設するものを手札から選ぶ

        // コストを支払う

        return false;
    }

}