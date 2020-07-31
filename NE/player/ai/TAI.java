package NE.player.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import NE.board.Board;
import NE.card.Card.CardCategory;
import NE.player.Player;

public class TAI implements IAI {

    private List<CardCategory> priority;
    private List<CardCategory> favor;
    private List<List<Integer>> memory = new ArrayList<>();// thinkの結果をそのターン終了まで格納しておくリスト。思考ロックを避けるため
    private List<WeightedCategory> weightedCategories = new ArrayList<>();

    private double huristicRate = 0.1;// ロジックを無視し思考ロックを打開する確率

    public TAI() {

        for (CardCategory category : CardCategory.values()) {
            weightedCategories.add(new WeightedCategory(category));
        }

    }

    // @Override
    // public List<Integer> think(Player self, Board board, int stucked) {
    // // スタックが0ならこのターンで初めての選択なので、前のターンのmemoryを消去
    // if (stucked == 0)
    // memory.clear();

    // // 優先順位を考える
    // priotize();

    // // List<Integer> list = new ArrayList<>(Arrays.asList(1, 4, 1, 3));
    // List<Integer> list = new ArrayList<>();
    // Random r = new Random();
    // list.add(r.nextInt(board.getBuildings().size()));

    // for (int i = 0; i < 10; i++) {
    // list.add(r.nextInt(self.getHands().size()));
    // }

    // memory.add(list);

    // return list;
    // }

    private void priotize() {

        this.priority = new ArrayList<CardCategory>(Arrays.asList(CardCategory.values()));
    }

    private class WeightedCategory implements Comparable {
        public WeightedCategory(CardCategory category) {
            this.category = category;
        }

        public CardCategory category;
        public int score = 0;

        @Override
        public int compareTo(Object other) {

            return Integer.compare(((WeightedCategory) other).score, this.score);
        }
    }

    @Override
    public List<Integer> thinkUseCard(Player self, Board board, int stucks) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int thinkBuild(Player self, Board board, Set<Integer> indexes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int thinkDiscard(Player self, Board board, Set<Integer> indexes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int thinkSell(Player self, Board board) {
        // TODO Auto-generated method stub
        return 0;
    }

}
