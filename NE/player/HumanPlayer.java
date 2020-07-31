package NE.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;

public class HumanPlayer extends Player {

    private static int id = 1;

    public HumanPlayer(Board board) {
        super(board);
        this.name = "Player-" + id;
        id++;
    }

    public boolean discard(Board board, List<Integer> indexesToDiscard, int cost) {

        // まず捨てるカード全部の参照を取得したい
        List<Card> cardsToDiscard = new ArrayList<>();

        List<Integer> modifiedIndexes = indexesToDiscard.stream().distinct().sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        System.out.println("Player#discard " + modifiedIndexes);

        for (Integer index : modifiedIndexes) {
            cardsToDiscard.add(this.hands.get(index));
        }

        // コストが足りているかチェック
        if (cardsToDiscard.size() < cost)
            return false;

        // 捨てる
        for (int i = 0; i < cost; i++) {
            board.getTrash().add(cardsToDiscard.get(i));
            this.hands.remove(cardsToDiscard.get(i));
        }
        // System.out.println("discard");
        // System.out.println(board.getTrash());

        return true;
    }
}