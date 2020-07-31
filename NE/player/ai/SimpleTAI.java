package NE.player.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.card.Card.CardCategory;
import NE.display.Display;
import NE.main.GameManager;
import NE.player.Player;

public class SimpleTAI implements IAI {

    private CardCategory topPriority = CardCategory.INDUSTRY;
    private List<Integer> actions = new ArrayList<>();

    private Player self;
    private Board board;
    private Card buildingToWork;

    // thinkの結果をそのターン終了まで格納しておくリスト。思考ロックを避けるため
    // 最後の選択カテゴリーを保存しておく、行動失敗したら別カテゴリーを模索
    private List<CardCategory> simpleMemory = new ArrayList<>();

    private double huristicRate = 0.1;// ロジックを無視し思考ロックを打開する確率

    public SimpleTAI() {

    }

    @Override
    public List<Integer> think(Player self, Board board, int stucked) {

        this.actions.clear();
        if (stucked == 0) {
            this.simpleMemory.clear();
        } else {
            // 不可能とわかったカテゴリーは記憶しておく
            // 使用する前にわかるときと使用しようとしてからわかるときが２パターン
            this.simpleMemory.add(this.topPriority);
        }
        this.self = self;
        this.board = board;

        boolean isPossible = false;
        do {
            // 優先順位を考える。可能かどうかは考慮しない。
            // memoryを参照し、すでに不可能だったカテゴリーは選ばない
            priotize();
            System.out.println("AI is intending: " + this.topPriority);
            System.out.println("AI remembers: " + this.simpleMemory);

            // 新規のpriorityに従って、そのカテゴリーの行動が実行可能か調べる
            // 可能ならactionsに、戻り値用のintを入れていく
            isPossible = pickArea(this.topPriority);

        } while (!isPossible);

        // カテゴリーに従って、さらに必要な選択をintとして加えていく必要がある
        // 仮に補完用の適当な数値を入れておく
        // 実際には建物に応じて、例えば建設したいカードや捨てたいカードを選択したい
        for (int i = 0; i < 10; i++) {
            this.actions.add(Display.myRandom(this.self.getHands().size()));
        }
        return this.actions;
    }

    private boolean pickArea(CardCategory category) {
        // 選びたいカテゴリーのカードを捜索して発見し次第そのエリアをintでactionにadd
        // まず自物件を検索
        for (int i = 0; i < this.self.getBuildings().size(); i++) {
            Card building = this.self.getBuildings().get(i);
            if (building.getCategory() == category && !building.isWorked()) {
                this.actions.add(1);// 1=自分の建物の意味
                this.actions.add(i);
                this.buildingToWork = building;
                return true;
            }
        }
        // 公共物件を最近建てられた物件から検索
        for (int i = this.board.getBuildings().size() - 1; i >= 0; i--) {
            Card building = this.board.getBuildings().get(i);
            if (building.getCategory() == category && !building.isWorked()) {
                this.actions.add(0);// 0=公共の建物の意味
                this.actions.add(i);
                this.buildingToWork = building;
                return true;
            }
        }
        // 不可能とわかったカテゴリーは記憶しておく
        this.simpleMemory.add(category);
        return false;
    }

    private void priotize() {

        int handSize = this.self.getHands().size();

        // ラストターンで手札の枚数が多いなら
        if (GameManager.getInstance().getCurrentTurn() == 9 && handSize >= 4) {
            if (isNewStrategy(CardCategory.MARKET)) {
                this.topPriority = CardCategory.MARKET;
                return;
            }
        }

        // 労働者を増やせるか
        if (isNewStrategy(CardCategory.EDUCATION) && this.self.getWorkers().size() < this.self.getWorkersLimits()) {
            this.topPriority = CardCategory.EDUCATION;
            return;
        }

        // 賃金が十分にあるか
        if (isNewStrategy(CardCategory.MARKET)) {
            if (this.self.getMoney() <= this.self.getWorkers().size() * GameManager.getInstance().getCurrentWage()
                    / 2) {
                this.topPriority = CardCategory.MARKET;
                return;
            }
        }

        // 手札の枚数はどうか
        if (handSize >= 3) {
            // 多いなら->建てられるか
            if (isNewStrategy(CardCategory.CONSTRUCTION)) {
                this.topPriority = CardCategory.CONSTRUCTION;
                return;
            }

        } else {
            // 少ないなら->ドローしたい->消費財
            // ターンのアクション数に余裕があるか
            if (this.self.getWorkers().size() - 2 >= this.self.getActionCount()) {
                if (isNewStrategy(CardCategory.AGRICULTURE)) {
                    this.topPriority = CardCategory.AGRICULTURE;
                    return;
                }
            } else {
                if (isNewStrategy(CardCategory.INDUSTRY)) {
                    this.topPriority = CardCategory.INDUSTRY;
                    return;
                }
            }
        }

        // 全て無理なら鉱山
        this.topPriority = CardCategory.INDUSTRY;
    }

    private boolean isNewStrategy(CardCategory category) {
        return this.simpleMemory.stream().allMatch(c -> c != category);
    }

    private class WeightedCategory {
        public WeightedCategory(CardCategory category, int score) {
            this.category = category;
            this.score = score;
        }

        public CardCategory category;
        public int score;
    }

    @Override
    public int discard(Player self, Board board) {
        List<Card> commodities = self.getHands().stream().filter(card -> card.getCategory() == CardCategory.COMMODITY)
                .collect(Collectors.toList());
        if (commodities.size() != 0) {
            return self.getHands().indexOf(commodities.get(0));
        }
        return 0;
    }

    @Override
    public int sell(Player self, Board board) {
        return 0;
    }

}