package NE.player;

import NE.board.Board;

public class HumanPlayer extends Player {

    private static int id = 1;

    public HumanPlayer(Board board) {
        super(board);
        this.name = "Player-" + id;
        id++;
    }
}