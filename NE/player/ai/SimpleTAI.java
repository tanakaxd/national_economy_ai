package NE.player.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.card.Card.CardCategory;
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
    private List<CardCategory> memory = new ArrayList<>();

    private double huristicRate = 0.1;// ロジックを無視し思考ロックを打開する確率

    public SimpleTAI() {

    }

    @Override
    public List<Integer> thinkUseCard(Player self, Board board, int stucked) {

        this.actions.clear();
        if (stucked == 0) {
            this.memory.clear();
        } else {
            // 不可能とわかったカテゴリーは記憶しておく
            // 使用する前にわかるときと使用しようとしてからわかるときが２パターン
            // ここは後者
            this.memory.add(this.topPriority);
        }
        this.self = self;
        this.board = board;

        boolean isPossible = false;
        do {
            // 優先順位を考える。可能かどうかは考慮しない。
            // memoryを参照し、すでに不可能だったカテゴリーは選ばない
            priotize();
            System.out.println("AI is intending: " + this.topPriority);
            System.out.println("AI remembers: " + this.memory);

            // 新規のpriorityに従って、そのカテゴリーの行動が実行可能か調べる
            // 可能ならactionsに、戻り値用のintを入れていく
            isPossible = pickArea(this.topPriority);

        } while (!isPossible);

        return this.actions;
    }

    private boolean pickArea(CardCategory category) {
        // 選びたいカテゴリーのカードを捜索して発見し次第そのエリアをintでactionにadd
        // 公共物件を最近建てられた物件から検索
        // TODO 検索精度を上げる
        for (int i = this.board.getBuildings().size() - 1; i >= 0; i--) {
            Card building = this.board.getBuildings().get(i);
            if (building.getCategory() == category && !building.isWorked()) {
                this.actions.add(0);// 0=公共の建物の意味
                this.actions.add(i);
                this.buildingToWork = building;
                return true;
            }
        }
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
        // 不可能とわかったカテゴリーは記憶しておく
        this.memory.add(category);
        return false;
    }

    private void priotize() {

        int handSize = this.self.getHands().size();

        // 終盤で手札の枚数が多いなら
        if (GameManager.getInstance().getCurrentTurn() >= 8 && handSize >= 4) {
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
            if (this.self.getMoney() <= this.self.getWorkers().size() * GameManager.getInstance().getCurrentWage()) {
                this.topPriority = CardCategory.MARKET;
                return;
            }
        }

        // 消費財が多く、action数に余裕があるか TODO 採石場を除外したい
        // if (isNewStrategy(CardCategory.INDUSTRY)
        // && this.self.getHands().stream().filter(c -> c.getCategory() ==
        // CardCategory.COMMODITY).count() >= 2
        // && this.self.getWorkers().size() - 1 >= this.self.getActionCount()) {
        // this.topPriority = CardCategory.INDUSTRY;
        // return;
        // }

        // 手札の枚数はどうか
        if (handSize >= 3) {
            // 多いなら->建てられるか
            // 手札が多くconstructionに失敗したらindustryになる。次のターンの建設に備えられるか？
            if (isNewStrategy(CardCategory.CONSTRUCTION)) {
                this.topPriority = CardCategory.CONSTRUCTION;
                return;
            }
        } else {
            // 少ないなら->ドローしたい->消費財
            // ターンのアクション数に余裕があるか
            if (isNewStrategy(CardCategory.AGRICULTURE)) {
                this.topPriority = CardCategory.AGRICULTURE;
                return;
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
        return this.memory.stream().allMatch(c -> c != category);
    }

    @Override
    public int thinkBuild(Player self, Board board, Set<Integer> indexesNotAllowed) {
        // 建てられるカードをフィルター
        List<Card> candidates = this.self.getHands().stream().filter(c -> c.isBuildable()).collect(Collectors.toList());
        if (candidates.size() == 0)
            return 0;

        Map<Card, Integer> performances = new LinkedHashMap<>();
        for (Card card : candidates) {
            performances.put(card, card.getValue() / card.getCost());
        }

        List<Card> filtered = candidates.stream().filter(c -> c.getCost() <= self.getHands().size() - 1)
                .sorted(Comparator.comparing(Card::getCost)).collect(Collectors.toList());

        if (filtered.size() == 0)
            return 0;

        System.out.println(filtered);
        return this.self.getHands().indexOf(filtered.get(0));

        // 費用対効果が一番高いものを選ぶ TODO 並び替えられてない
        // Map<Card, Integer> result = performances.entrySet().stream()
        // .sorted(Map.Entry.<Card, Integer>comparingByValue().reversed())
        // .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        // result.forEach((k, v) -> {
        // System.out.println(k);
        // System.out.println(v);
        // });

        // Object[] cards = result.keySet().toArray();
        // for (Object card : cards) {
        // System.out.println(card);
        // }

        // return this.self.getHands().indexOf(cards[0]);

    }

    @Override
    public int thinkDiscard(Player self, Board board, Set<Integer> indexesNotAllowed) {
        List<Card> commodities = self.getHands().stream().filter(card -> card.getCategory() == CardCategory.COMMODITY)
                .filter(card -> indexesNotAllowed.stream().noneMatch(index -> index == self.getHands().indexOf(card)))
                .collect(Collectors.toList());
        if (commodities.size() != 0) {
            return self.getHands().indexOf(commodities.get(0));
        }
        return new Random().nextInt(this.self.getHands().size());
    }

    @Override
    public int thinkSell(Player self, Board board) {
        // TODO Auto-generated method stub
        return 0;
    }

}