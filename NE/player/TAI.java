package NE.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import NE.board.Board;
import NE.card.Card.CardCategory;

public class TAI implements IThinkable {

    private List<CardCategory> priority;
    private List<List<Integer>> memory;

    @Override
    public List<Integer> think(Player self, Board board, int stucked) {
        // 優先順位を考える

        // List<Integer> list = new ArrayList<>(Arrays.asList(1, 4, 1, 3));
        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        list.add(r.nextInt(board.getBuildings().size()));

        for (int i = 0; i < 10; i++) {
            list.add(r.nextInt(self.getHands().size()));
        }

        return list;
    }

    @Override
    public List<Integer> discard(Player self, Board board, int stucked) {
        // TODO Auto-generated method stub
        return null;
    }

}
