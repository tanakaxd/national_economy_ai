package NE.player.ai;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public interface IAI {

    // どのエリアで、どのカードをどう使うかを決める。その決定をリストとして返す
    public abstract List<Integer> think(Player self, Board board, int stucked);

    // ターン終了時に捨てるカードを選ぶ。戻り値は手札内のindex
    public abstract int discard(Player self, Board board);

    // 賃金支払いのため売却する物件を選ぶ。戻り値は自物件内のindex
    public abstract int sell(Player self, Board board);

}
