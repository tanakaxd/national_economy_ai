package NE.player.ai.tai;

import static NE.card.Card.CardCategory.AGRICULTURE;
import static NE.card.Card.CardCategory.CONSTRUCTION;
import static NE.card.Card.CardCategory.EDUCATION;
import static NE.card.Card.CardCategory.INDUSTRY;
import static NE.card.Card.CardCategory.MARKET;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import NE.board.Board;
import NE.card.Card;
import NE.card.Card.CardCategory;
import NE.data.CsvImporter;
import NE.main.GameManager;
import NE.main.Main;
import NE.player.Player;
import NE.player.ai.IAI;
import NE.player.ai.tai.TAIGeneLoader.GeneMode;

public class TAI implements IAI {

    // #region
    // 思考順序

    // A
    // 使えるカードをすべてピックする。IDの重複は同一カードとみなしてフィルターする。
    // List<Card>として集める

    // B
    // 以下4つの要素でカードごとの価値を計算する
    // 1. カードの基礎価値=baseEvaluation 0-50
    // 2. 状況（ターン数、アクション数、手札枚数、所有物件、労働者数、GDP）に基づいたカテゴリー価値=categoryValue 0-50
    // 2. 状況（ターン数、アクション数、手札枚数、所有物件、労働者数、GDP）に基づいたカード個別の係数=situationalCoefficient 0-2
    // 4. カテゴリー価値を補正するPERSONALITY=personalityFavor 0.5-1.5
    // PERSONALITY: favor[category, huristicRate]
    // => cardValue =
    // (baseEvaluation + categoryValue*personalityFavor) * situationalCoefficient
    // ((0~50) + (0~50)*(0.5~1.5))*(0~2)

    // 具体的に
    // 状況１：ターン1、残りアクション数２、手札３、所有物件なし、労働者数２、GDP５
    // 状況２：たーん５、残りアクション数４、手札０、所有物件なし、労働者数５、GDP３０

    // 芋畑1：(35 + 15*1) * 0.1 = 5
    // 芋畑2：(35 + 45*1) * 1.5 = 120

    // C
    // 評価順にAのリストを並び替える。
    // この場合、評価値を記憶していない

    // D
    // 順に使用を試す。一度試したものは記憶しておく

    // できることなら、カードをカテゴリーでひとくくりにするのではなく一つ一つの価値を計算した方がよい
    // 例えば、鉱山を使った後ならIndustryカテゴリーの中でも鉄工所の価値は上がる
    // 工業団地や食品工場はコストが変動するため、評価も再計算する必要がある
    // カードごとに評価関数を作る方法が一つ、カードの情報を読み取って動的にカード効果を評価する関数を作るのが一つ
    // #endregion

    private final Map<Integer, Integer> CARDS_BASE_EVALUATION = CsvImporter.getInstance().getCardData();// idと基礎評価
    private Map<CardCategory, Integer> categoryValue = new HashMap<>();
    private LinkedHashMap<CardCategory, Integer> PERSONALITY_FAVOR;
    private List<Card> orderedCards = new ArrayList<>();// thinkの結果をそのターン終了まで格納しておくリスト。思考ロックを避けるため
    private double huristicRate = 0.1;// ロジックを無視し思考ロックを打開する確率
    private int categoryValueCap = 50;
    private Player self;
    private Board board;
    private Card buildingToWork;
    private Card secondCardToBuild;// for地球建設

    public TAI(GeneMode geneMode) {

        for (CardCategory category : CardCategory.values()) {
            categoryValue.put(category, 0);
        }

        PERSONALITY_FAVOR = new LinkedHashMap<CardCategory, Integer>(
                TAIGeneLoader.getInstance().getPersonalityData(geneMode));

    }

    public TAI(int... nums) {

        for (CardCategory category : CardCategory.values()) {
            categoryValue.put(category, 0);
        }

        PERSONALITY_FAVOR = new LinkedHashMap<CardCategory, Integer>(
                TAIGeneLoader.getInstance().getPersonalityData(nums));

    }

    public TAI() {
        for (CardCategory category : CardCategory.values()) {
            categoryValue.put(category, 0);
        }

        PERSONALITY_FAVOR = new LinkedHashMap<CardCategory, Integer>(
                TAIGeneLoader.getInstance().getPersonalityData(GeneMode.GENETIC_ALGORITHM));
    }

    private int calcCardValueToUse(Card card) {
        // 以下4つの要素でカードごとの価値を計算する
        // 1. カードの基礎価値=baseEvaluation 0-50
        // 2. 状況（ターン数、アクション数、手札枚数、所有物件、労働者数、GDP）に基づいたカテゴリー価値=categoryValue 0-50
        // 2. 状況（ターン数、アクション数、手札枚数、所有物件、労働者数、GDP）に基づいたカード個別の係数=situationalCoefficient 0-2
        // 4. カテゴリー価値を補正するPERSONALITY=personalityFavor 0.5-1.5
        // PERSONALITY: favor[category, huristicRate]
        // => cardValue =
        // (baseEvaluation + categoryValue*personalityFavor) * situationalCoefficient
        // ((0~50) + (0~50)*(0.5~1.5))*(0~2)

        double value = (this.CARDS_BASE_EVALUATION.get(card.getId())
                + this.categoryValue.get(card.getCategory()) * this.PERSONALITY_FAVOR.get(card.getCategory()) / 100)
                * calcSituationCoefficientToWork(card.getId());

        return (int) value;
    }

    private void addCategoryValue(CardCategory category, int amount) {
        this.categoryValue.put(category, this.categoryValue.get(category) + amount);
    }

    private void capCategoryValue() {
        this.categoryValue.forEach((k, v) -> {
            this.categoryValue.put(k, Math.max(Math.min(v, this.categoryValueCap), -this.categoryValueCap));
        });
    }

    private void calcCategoryValue() {

        // 全てゼロでリセット
        for (CardCategory category : this.categoryValue.keySet()) {
            this.categoryValue.put(category, 0);
        }

        // AGRICULTURE, CONSTRUCTION, INDUSTRY, MARKET, EDUCATION
        // 状況（ターン数、アクション数、手札枚数、所有物件、労働者数、GDP）

        // ターン数をチェック
        if (getCurrentTurn() <= 3) {
            // 序盤 CONSTRUCTION
            addCategoryValue(CONSTRUCTION, 50);
            addCategoryValue(MARKET, -25);
        } else if (getCurrentTurn() <= 7) {
            // 中盤 EDUCATION
            addCategoryValue(CONSTRUCTION, 25);
            addCategoryValue(EDUCATION, 50);
        } else if (getCurrentTurn() <= 8) {
            // 終盤 MARKET
            addCategoryValue(MARKET, 25);
        } else {
            // ラスト
            addCategoryValue(MARKET, 50);
            addCategoryValue(EDUCATION, -50);

        }

        // 残りアクション数
        if (getActionsLeft() >= 4) {
            addCategoryValue(INDUSTRY, 50);
            addCategoryValue(AGRICULTURE, 50);
        } else if (getActionsLeft() >= 2) {
            addCategoryValue(INDUSTRY, 25);
            addCategoryValue(AGRICULTURE, 25);
        } else if (getActionsLeft() == 1) {
            addCategoryValue(MARKET, 25);
            addCategoryValue(CONSTRUCTION, 25);
            addCategoryValue(INDUSTRY, -25);
            addCategoryValue(AGRICULTURE, -25);
        }

        // 手札枚数
        // 多い CONSTRUCTION/MARKET
        // 少ない AGRICULTURE/INDUSTRY
        if (getHandSize() >= 8) {
            addCategoryValue(CONSTRUCTION, 75);
        } else if (getHandSize() >= 6) {
            addCategoryValue(CONSTRUCTION, 50);
        } else if (getHandSize() >= 4) {
            addCategoryValue(CONSTRUCTION, 25);
            addCategoryValue(INDUSTRY, 25);
        } else if (getHandSize() <= 2) {
            addCategoryValue(AGRICULTURE, 50);
            addCategoryValue(INDUSTRY, 50);
        }

        // 消費財が多く、アクション数が残っている→industry

        // 所有物件/資金
        // 多い MARKETは後回し
        if (getAffordableProperty() >= 25) {
            addCategoryValue(EDUCATION, 75);
            addCategoryValue(MARKET, -25);
        } else if (getAffordableProperty() >= 10) {
            addCategoryValue(EDUCATION, 50);
        } else if (getAffordableProperty() >= 5) {
            addCategoryValue(EDUCATION, 25);
        } else if (getAffordableProperty() >= -5) {
            addCategoryValue(CONSTRUCTION, 50);
            addCategoryValue(MARKET, 25);
            addCategoryValue(EDUCATION, -25);
        } else {
            addCategoryValue(CONSTRUCTION, 25);
            addCategoryValue(MARKET, 50);
            addCategoryValue(EDUCATION, -50);
        }

        // 労働者数
        // ターンと比較して少ない EDUCATION
        // 差があるほど優先度は増すが、一気に得る手段もある
        // 4,6,8
        if (getWorkerCounts() >= 5) {
            addCategoryValue(EDUCATION, -50);
        } else if (getWorkerCounts() >= 4) {
            addCategoryValue(EDUCATION, -25);
        } else {
            addCategoryValue(EDUCATION, 25);
        }

        // GDPが多い MARKET
        if (getGDP() >= 100) {
            addCategoryValue(MARKET, 50);
        } else if (getGDP() >= 50) {
            addCategoryValue(MARKET, 25);
        } else if (getGDP() <= 9) {
            addCategoryValue(MARKET, -50);
            addCategoryValue(CONSTRUCTION, 25);
        }
        System.out.println("before cap: " + this.categoryValue);

        // cap
        // capCategoryValue();

        System.out.println("after cap:" + this.categoryValue);

        // this.categoryValue.forEach((k, v) -> System.out.println(k + " = " + v));

    }

    private double calcSituationCoefficientToWork(int id) {
        double result = 0;
        switch (id) {
            case 0:// 芋畑
                result = getHandSize() >= 3 ? 0 : getHandSize() == 2 ? 0.5 : 1.5;
                return result;
            case 2:// 養殖場
                result = this.self.getHands().stream().anyMatch(c -> c.getCategory() == CardCategory.COMMODITY) ? 1.5
                        : 1;
                return result;
            case 3:// 醸造所
                result = getCurrentTurn() == 9 ? 0 : 1;
                return result;
            case 22:// 製鉄所
                result = this.self.getUseMine() ? 2 : 0;
                return result;
            case 37:// 観光牧場
                result = this.self.getBuildings().stream().filter(c -> c.getCategory() == CardCategory.COMMODITY)
                        .count() * 0.5;
                return result;
            case 43:// 専門学校
                result = getCurrentTurn() == 8 ? 2 : 0;
                return result;
            default:
                return 1;
        }
    }

    private double calcSituationCoefficientToBuild(int id) {
        double result = 1;
        // TODO 邪道かも
        if (id >= 50 && id <= 59) {// facility
            if (getAffordableProperty() >= 0) {
                result = 2;
            } else {
                result = 0;
            }
        }
        switch (id) {
            case 23:// 食品工場
                int count = (int) this.self.getBuildings().stream().filter(c -> c.isAgriculture()).count();
                result = count >= 2 ? 3 : count >= 1 ? 2 : 1;
                return result;
            case 26:// 工業団地
                int count2 = (int) this.self.getBuildings().stream().filter(c -> c.isFactory()).count();
                result = 1 + count2 * 0.5;
                return result;
            case 58:// 大聖堂
                result = this.self.getVictoryPoint() >= 5 ? 3 : 0;
                return result;
            default:
                return result;
        }

    }

    private List<Card> getBuildingsFromDigit(int digit) {
        switch (digit) {
            case 0:
                return this.board.getBuildings();
            case 1:
                return this.self.getBuildings();
            default:
                return null;
        }
    }

    @Override
    public List<Integer> thinkUseCard(Player self, Board board, int stucks) {

        this.self = self;
        this.board = board;

        // stucksが0ならこのアクションで初めての選択なので思考して、結果を記憶しておく
        if (stucks == 0) {
            System.out.println("AffordableProperty: " + getAffordableProperty());
            System.out.println(this.PERSONALITY_FAVOR);

            // A
            // 使えるカードをすべてピックする。IDの重複は同一カードとみなしてフィルターする。
            // アクションの最初に計算したカードリストを保存しておけばいちいちメモリーから除外すべきカードを選ぶ必要はない
            // List<Card>として集める
            List<Integer> duplicateIDs = new ArrayList<>();
            // List<Card> cardsFromMemory = this.memory.stream()
            // .map(list ->
            // getBuildingsFromDigit(list.get(0)).get(list.get(1))).collect(Collectors.toList());

            // peekによる副作用を利用して、外部の変数とやりとりしている。
            // 本来関数型プログラミングは副作用を持つべきでないので、一部では堕落した方法とみなされるらしい
            List<Card> possibleCards = Stream.concat(board.getBuildings().stream(), self.getBuildings().stream())
                    // .filter(c -> cardsFromMemory.stream().noneMatch(cardFromMemory -> c.getId()
                    // == cardFromMemory.getId()))
                    .filter(c -> !c.isWorked() && c.getCategory() != CardCategory.FACILITY)
                    .filter(c -> duplicateIDs.stream().noneMatch(id -> id == c.getId()))
                    .peek(c -> duplicateIDs.add(c.getId())).collect(Collectors.toList());

            System.out.println("possibleCards: " + possibleCards);

            // B
            // 状況からカテゴリーの価値を計算する
            calcCategoryValue();

            // C
            // 以下4つの要素でカードごとの価値を計算し、sortする
            // 1. カードの基礎価値=baseEvaluation 0-50
            // 2. 状況（ターン数、アクション数、手札枚数、所有物件、労働者数、GDP）に基づいたカテゴリー価値=categoryValue 0-50
            // 2. 状況（ターン数、アクション数、手札枚数、所有物件、労働者数、GDP）に基づいたカード個別の係数=situationalCoefficient 0-2
            // 4. カテゴリー価値を補正するPERSONALITY=personalityFavor 0.5-1.5
            // PERSONALITY: favor[category, huristicRate]
            // => cardValue =
            // (baseEvaluation + categoryValue*personalityFavor) * situationalCoefficient
            // ((0~50) + (0~50)*(0.5~1.5))*(0~2)

            // 具体的に
            // 状況１：ターン1、残りアクション数２、手札３、所有物件なし、労働者数２、GDP５
            // 状況２：たーん５、残りアクション数４、手札０、所有物件なし、労働者数５、GDP３０

            // 芋畑1：(35 + 15*1) * 0.1 = 5
            // 芋畑2：(35 + 45*1) * 1.5 = 120

            // D
            // 再計算の必要が無いよう次の自分の番がくるまでは記憶しておく。
            this.orderedCards = possibleCards.stream()
                    .sorted((c1, c2) -> calcCardValueToUse(c2) - calcCardValueToUse(c1)).peek(c -> {
                        System.out.println(c + " = " + calcCardValueToUse(c));
                    }).collect(Collectors.toList());

        }

        // E
        // 順に使用を試す
        List<Integer> decision = new ArrayList<>();

        Card cardToWork = this.orderedCards.get(stucks);
        this.buildingToWork = cardToWork;
        if (this.board.getBuildings().contains(cardToWork)) {
            decision.add(0);
            decision.add(this.board.getBuildings().indexOf(cardToWork));
        } else {
            decision.add(1);
            decision.add(this.self.getBuildings().indexOf(cardToWork));
        }

        return decision;
    }

    @Override
    public int thinkBuild(Player self, Board board, Set<Integer> indexesNotAllowed) {
        // 建てられるカードをフィルター
        List<Card> candidates = this.self.getHands().stream()
                .filter(c -> c.isBuildable()
                        && indexesNotAllowed.stream().noneMatch(index -> index == this.self.getHands().indexOf(c)))
                .collect(Collectors.toList());
        if (candidates.isEmpty())
            return 0;

        // 地球建設の場合、size()-2のコストになる組み合わせを選ぶのが最適
        if (this.buildingToWork.getId() == 14) {
            if (this.secondCardToBuild == null) {// 地球建設で一つ目の建物を聞かれた場合
                // size()-2のコストになる組み合わせを選びたい

                // まずなるべきコストを計算
                int totalCostToBe = self.getHands().size() - 2;

                // 1カードのコストの最低値が１だと期待する
                // 例えば1-6コストのカードが一枚ずつ手札にあるとする
                // トータルで4コストの組み合わせを探す
                // そうなるには4-1=3コスト以下のカードが候補になる
                // candidates = 1,2,3
                List<Card> filtered = candidates.stream().filter(c -> c.getCost(self) <= totalCostToBe - 1).sorted((e1,
                        e2) -> this.CARDS_BASE_EVALUATION.get(e1.getId()) - this.CARDS_BASE_EVALUATION.get(e2.getId()))
                        .collect(Collectors.toList());

                for (int i = 0; i < filtered.size(); i++) {
                    for (int j = i + 1; j < filtered.size(); j++) {// 自分（indexがi）より一つ先のindexのカードをすべて調べる
                        Card firstCandidate = filtered.get(i);
                        Card secondCandidate = filtered.get(j);
                        if (firstCandidate.getCost(self) + secondCandidate.getCost(self) == totalCostToBe) {
                            // 二番目に建設するカードを保存しておく
                            this.secondCardToBuild = secondCandidate;
                            // 一番目のやつをreturn
                            return self.getHands().indexOf(firstCandidate);
                        }
                    }
                }

                // 該当する組み合わせがない場合
                // stuckするが呼び出し側で抜けられる
                return 0;

            } else {// 地球建設で二つ目の建物を聞かれた場合
                int index = self.getHands().indexOf(this.secondCardToBuild);
                this.secondCardToBuild = null;
                return index;
            }

        } else {
            // 地球建設以外の場合
            // TODO コストが足りるものの中で、costが高い順に、ソート順評価に状況に応じた係数をかける
            // 建てられるものをフィルターにかける→Cardと評価のMapにする→評価が一番高いCardを取得
            // ソートはいらない、O(n)のO(nlogn)差が出るはず
            List<Card> filtered = candidates.stream().filter(c -> c.getCost(self) <= self.getHands().size() - 1)
                    .sorted((e1,
                            e2) -> (int) (e2.getCost(self) * calcSituationCoefficientToBuild(e2.getId())
                                    - e1.getCost(self) * calcSituationCoefficientToBuild(e1.getId())))
                    .collect(Collectors.toList());

            if (filtered.isEmpty())
                return 0;

            System.out.println("priority to build: " + filtered);
            return this.self.getHands().indexOf(filtered.get(0));
        }

    }

    @Override
    public int thinkDiscard(Player self, Board board, Set<Integer> indexesNotAllowed) {
        // candidates がゼロ個になることはないと想定している。外部のメソッド次第
        List<Card> candidates = self.getHands().stream()
                .filter(card -> indexesNotAllowed.stream().noneMatch(index -> index == self.getHands().indexOf(card)))
                .collect(Collectors.toList());

        System.out.println("candidates: " + candidates);

        List<Card> commodities = candidates.stream().filter(card -> card.getCategory() == CardCategory.COMMODITY)
                .collect(Collectors.toList());
        if (!commodities.isEmpty()) {
            return self.getHands().indexOf(commodities.get(0));
        } else {
            return self.getHands().indexOf(candidates.get(new Random().nextInt(candidates.size())));
        }
    }

    @Override
    public int thinkSell(Player self, Board board) {
        // 売れるカードがない場合はメソッドが呼び出されないと想定
        return self.getBuildings().indexOf(self.getBuildings().stream()
                .filter(c -> c.getCategory() != CardCategory.FACILITY).collect(Collectors.toList()).get(0));
    }

    @Override
    public String toString() {

        return this.PERSONALITY_FAVOR.entrySet().stream()
                // .collect(Collectors.toMap(p -> p.getKey(), p -> String.format("%d",
                // p.getValue()))).toString();
                .collect(Collectors.toList()).toString();
    }

    // #region shorthand getter
    private int getHandSize() {
        return this.self.getHands().size();
    }

    private int getCurrentTurn() {
        return GameManager.getInstance().getCurrentTurn();
    }

    private int getActionsLeft() {
        return (int) this.self.getWorkers().stream().filter(w -> w.isWorkable()).count();
    }

    private List<Card> getOwnedBuildings() {
        return this.self.getBuildings();
    }

    private int getAffordableProperty() {
        int value = 0;
        for (Card card : this.self.getBuildings().stream().filter(c -> c.getCategory() != CardCategory.FACILITY)
                .collect(Collectors.toList())) {
            value += card.getValue();
        }
        value += this.self.getMoney();
        value -= getTotalWages();
        return value;
    }

    private int getWorkerCounts() {
        return this.self.getWorkers().size();
    }

    private int getGDP() {
        return GameManager.getInstance().getGDP();
    }

    private int getTotalWages() {
        return getWorkerCounts() * GameManager.getInstance().getCurrentWage();
    }

    // #endregion
    private class CategoryValue {
        public CategoryValue(CardCategory category, int score) {
            this.category = category;
            this.score = 0;
        }

        public CardCategory category;
        public int score;

        public void addScore(int amount) {
            this.score += amount;
        }

        public void reset() {
            this.score = 0;

        }
    }

    public Map<CardCategory, Integer> getPERSONALITY_FAVOR() {
        return PERSONALITY_FAVOR;
    }

}
