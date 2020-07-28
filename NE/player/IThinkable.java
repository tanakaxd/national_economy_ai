package NE.player;

import java.util.ArrayList;

import NE.board.Board;

public interface IThinkable {

    public abstract ArrayList<Integer> think(Board board, boolean stucked);

}
