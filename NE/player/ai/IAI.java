package NE.player.ai;

import java.util.List;
import java.util.Set;

import NE.board.Board;
import NE.player.Player;

public interface IAI {

    // 「使いたいカード」
    // どのエリアで、どのカードを使うか決める。その決定をリストとして返す。
    // stuckは今の自分の番ですでに何回失敗しているか。
    public abstract List<Integer> thinkUseCard(Player self, Board board, int stucks);

    // 「建てたいカード」
    public abstract int thinkBuild(Player self, Board board, Set<Integer> indexesNotAllowed);

    // 「捨てたいカード」
    // 捨てるカードを選ぶ。戻り値は手札内のindex
    public abstract int thinkDiscard(Player self, Board board, Set<Integer> indexesNotAllowed);

    // 「売りたいカード」
    // 賃金支払いのため売却する物件を選ぶ。戻り値は自物件内のindex
    public abstract int thinkSell(Player self, Board board);

}

/*
 * 
 * AIの方針： AIに連続的な選択を意識させたかったから、一連の選択を受け取るという目的で戻り値をリストで作ったが、その必要があるのか疑問。
 * その都度「使いたいカード」「建てたいカード」「捨てたいカード」「売りたいカード」という分解された問いを投げかける構造にしてもいいかもしれない
 * つまり、その問い一つ一つに対応したメソッドをAI側は持っていて、カードの効果はその組み合わせで実現するということ
 * 例えば、建築カードなら「建てたいカード」を聞いて、そのコスト分だけ「捨てたいカード」を聞くという設計
 * 
 * 戻り値はリストにしておくという手もある。その場合、優先順位をつけたindex。でも、indexがずれるか？
 * 
 * 選択と実際の処理は別物 選択肢が実行不可能な場合がある
 * 
 * 問いがどのカードによってもたらされているのかを判断できるように、引数としてCardが送られてくる設計もありか
 * 
 */
