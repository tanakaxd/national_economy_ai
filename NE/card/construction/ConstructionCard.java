package NE.card.construction;

import NE.board.Board;
import NE.card.Card;
import NE.player.Player;

public abstract class ConstructionCard extends Card {

    protected int amountsToBuild;

    public ConstructionCard() {

    }

    // template method
    @Override
    public final boolean apply(Player player, Board board) {
        if ((player.getHands().stream().filter(c -> c.isBuildable()).count() < this.amountsToBuild) || this.isWorked)
            return false;

        return doApply(player, board);
    }

    public abstract boolean doApply(Player player, Board board);

    public int getAmountsToBuild() {
        return amountsToBuild;
    }

}