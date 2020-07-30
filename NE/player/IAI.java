package NE.player;

import java.util.List;

import NE.board.Board;

public interface IAI {

    // 自分の番に労働者を働かせる場所を選び、そのカードに応じて選択肢も入れたリストを返す
    public abstract List<Integer> think(Player self, Board board, int stucked);

    // ターン終了時に捨てるカードを選ぶ
    public abstract int discard(Player self, Board board);

    // 賃金支払いのため売却する物件を選ぶ
    public abstract int sell(Player self, Board board);

}
