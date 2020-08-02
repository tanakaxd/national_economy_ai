package NE.card.construction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class ConstructionCardE extends ConstructionCard {

    public ConstructionCardE() {
        this.id = 14;
        this.name = "地球建設";
        this.category = CardCategory.CONSTRUCTION;
        this.cost = 4;
        this.value = 16;
        this.description = "";
        this.isAgriculture = false;
        this.isFactory = false;
        this.isFacility = false;
        this.isBuildable = true;
        this.isCommons = false;
        this.isWorked = false;

        this.amountsToBuild = 2;
    }

    @Override
    public boolean doApply(Player player, Board board) {

        List<Card> hands = player.getHands();

        Display.printChoices(hands);

        List<Integer> indexesToBuild = player.askBuild(board, this.amountsToBuild, this);
        // 一枚だけ選択して返してくる可能性がある
        if (indexesToBuild.size() < this.amountsToBuild)
            return false;

        int totalCost = indexesToBuild.stream().map(i -> hands.get(i).getCost(player)).mapToInt(Integer::intValue)
                .sum();

        if (totalCost + this.amountsToBuild > hands.size())
            return false;

        // TODO askした段階で、加工されたリストが送られてくると保証できれば、この処理は簡略化できる可能性がある
        List<Integer> indexesToDiscard = player.askDiscard(board, totalCost, indexesToBuild).stream().distinct()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        if (indexesToDiscard.size() < totalCost)
            return false;

        // 建てるカードの参照を取得
        List<Card> cardsToBuild = indexesToBuild.stream().map(index -> hands.get(index)).collect(Collectors.toList());

        // 全てのカードが建設可能かチェック。
        if (cardsToBuild.stream().anyMatch(c -> !c.isBuildable()))
            return false;

        // 建てるカードと捨てるカードがかぶらないように。聞く段階では別々のメソッドを使用しているため、この処理はここで両者の情報を統合して行う必要がある。
        // ここでチェックすれば例外を防ぐこと自体は可能だが、AIがstuckし続けて何も建築できないという事態は防げない。
        // なので、Player#askDiscardの継承先でかぶらないようにチェックする設計にした
        // よって、ここでチェックしているのは念のためとプレイヤー用
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

        // draw
        if (hands.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                player.draw(board);
            }
        }

        this.isWorked = true;
        return true;

    }
}