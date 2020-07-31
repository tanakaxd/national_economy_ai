package NE.player.ai;

import java.util.List;

import NE.board.Board;
import NE.player.Player;

public interface IAI {

    // どのエリアで、どのカードをどう使うかを決める。その決定をリストとして返す
    public abstract List<Integer> think(Player self, Board board, int stucked);

    // ターン終了時に捨てるカードを選ぶ。戻り値は手札内のindex
    public abstract int discard(Player self, Board board);

    public abstract int discard(Player self, Board board, int indexesNotAllowed);

    // 賃金支払いのため売却する物件を選ぶ。戻り値は自物件内のindex
    public abstract int sell(Player self, Board board);

}

/*
 * 
 * AIの方針： AIに連続的な選択を意識させたかったから、一連の選択を受け取るという目的で戻り値をリストで作ったが、その必要があるのか疑問
 * その都度「使いたいカード」「建てたいカード」「捨てたいカード」「売りたいカード」という分解された問いを投げかける構造にしてもいいかもしれない
 * つまり、その問い一つ一つに対応したメソッドをAI側は持っていて、カードの効果はその組み合わせで実現するということ
 * 例えば、建築カードなら「建てたいカード」を聞いて、そのコスト分だけ「捨てたいカード」を聞くという設計
 * 
 */
