package NE.card.construction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class ConstructionCardA extends ConstructionCard {

    public ConstructionCardA() {
        this.id = 10;
        this.category = CardCategory.CONSTRUCTION;
        this.name = "大工";
        this.cost = 1;
        this.value = 0;
        this.isWorked = false;
        this.isBuildable = false;
        this.isCommons = true;

        this.minHands = 1;
        this.amountsToBuild = 1;
    }

    @Override
    public boolean apply(Player player, Board board) {
        List<Card> hands = player.getHands();

        if (hands.size() < this.minHands || this.isWorked)
            return false;

        Display.printChoices(hands);

        List<Integer> indexesToBuild = player.askBuild(board, this);
        int indexToBuild = indexesToBuild.get(0);

        int cost = hands.get(indexToBuild).getCost();
        if (cost + this.amountsToBuild > hands.size())
            return false;

        // TODO askした段階で、加工されたリストが送られてくると保証できれば、この処理は簡略化できる可能性がある
        List<Integer> indexesToDiscard = player.askDiscard(board, cost).stream().distinct()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        if (indexesToDiscard.size() < cost)
            return false;

        // 建てるカードの参照を取得
        List<Card> cardsToBuild = indexesToBuild.stream().map(index -> hands.get(index)).collect(Collectors.toList());

        // 全てのカードが建設可能かチェック
        if (cardsToBuild.stream().anyMatch(c -> !c.isBuildable()))
            return false;

        // 建てるカードと捨てるカードがかぶらないように。聞く段階では別々のメソッドを使用しているため、この処理はここで両者の情報を統合して行う必要がある。
        boolean isViableChoice = indexesToDiscard.stream()
                .noneMatch(d -> indexesToBuild.stream().anyMatch(b -> d == b));
        if (!isViableChoice)
            return false;

        // ここまで来ればdiscardがfalseになるパターンはない。今後発生する可能性はある TODO
        // if (!player.discard(board, modifiedIndexesToDiscard, cost))
        // return false;

        // 捨てる
        for (Integer integer : indexesToDiscard) {
            player.discard(board, integer);
        }

        // 建てる
        for (Card c : cardsToBuild) {
            player.build(c);
        }
        this.isWorked = true;
        return true;
    }
}