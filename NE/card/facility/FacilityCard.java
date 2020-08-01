package NE.card.facility;

import NE.board.Board;
import NE.card.Card;
import NE.player.Player;

public abstract class FacilityCard extends Card {

    public abstract int calcBonus(Player player);

}