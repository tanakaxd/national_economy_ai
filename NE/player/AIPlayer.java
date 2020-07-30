package NE.player;

import NE.board.Board;

public class AIPlayer extends Player {

    private IAI brain;

    private static int id = 1;

    public AIPlayer(Board board, IAI brain) {
        super(board);
        this.brain = brain;
        this.name = "AI-" + id + ":brain=" + this.brain.getClass().getName();
        id++;
    }

    public IAI getBrain() {
        return brain;
    }

    public void setBrain(IAI brain) {
        this.brain = brain;
    }

}