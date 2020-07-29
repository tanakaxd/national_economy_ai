package NE.player;

import java.util.List;

import NE.board.Board;

public interface IThinkable {

    public abstract List<Integer> think(Player self, Board board, int stucked);

    public abstract List<Integer> discard(Player self, Board board, int stucked);

}
