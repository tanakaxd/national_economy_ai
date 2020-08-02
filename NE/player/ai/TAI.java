package NE.player.ai;

import static NE.card.Card.CardCategory.AGRICULTURE;
import static NE.card.Card.CardCategory.CONSTRUCTION;
import static NE.card.Card.CardCategory.EDUCATION;
import static NE.card.Card.CardCategory.FACILITY;
import static NE.card.Card.CardCategory.INDUSTRY;
import static NE.card.Card.CardCategory.MARKET;

import java.util.ArrayList;
import java.util.HashMap;
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
import NE.player.Player;

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

    private final Map<Integer, Integer> CARDS_BASE_EVALUATION = CsvImporter.loadCardData();// idと基礎評価
    private Map<CardCategory, Integer> categoryValue = new HashMap<>();
    private final Map<CardCategory, Integer> PERSONALITY_FAVOR = new HashMap<CardCategory, Integer>() {
        private static final long serialVersionUID = -8080332401300985295L;
        {
            put(AGRICULTURE, 1);
            put(CONSTRUCTION, 1);
            put(INDUSTRY, 1);
            put(MARKET, 1);
            put(EDUCATION, 1);
            put(FACILITY, 1);
        }
    };
    private List<Card> orderedCards = new ArrayList<>();// thinkの結果をそのターン終了まで格納しておくリスト。思考ロックを避けるため
    private double huristicRate = 0.1;// ロジックを無視し思考ロックを打開する確率
    private Player self;
    private Board board;
    private Card buildingToWork;
    private Card secondCardToBuild;// for地球建設

    public TAI() {
        for (CardCategory category : CardCategory.values()) {
            categoryValue.put(category, 0);
        }

        // System.out.println(this.CARDS_BASE_EVALUATION);
        // System.out.println(this.personalityFavor);
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
                + calcCategoryValue(card.getCategory()) * this.PERSONALITY_FAVOR.get(card.getCategory()))
                * calcSituationCoefficient(card.getId());

        return (int) value;
    }

    private int calcCategoryValue(CardCategory category) {
        switch (category) {
            case AGRICULTURE:
                return calcCategoryAgriculture();
            default:
                return 0;

        }
    }

    private int calcCategoryAgriculture() {
        // 手札枚数が
        return 0;
    }

    private double calcSituationCoefficient(int id) {
        double result = 0;
        switch (id) {
            case 0:
                result = getHandSize() >= 3 ? 0 : getHandSize() == 2 ? 0.5 : 1.5;
                return result;
            default:
                return 1;
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

            this.orderedCards = possibleCards.stream()
                    .sorted((c1, c2) -> calcCardValueToUse(c2) - calcCardValueToUse(c1)).collect(Collectors.toList());
        }

        // D
        // 順に使用を試す。一度試したものは記憶しておく
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
        if (candidates.size() == 0)
            return 0;

        // コストが足りるものの中で高い順に
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
                List<Card> filtered = candidates.stream().filter(c -> c.getCost(self) <= totalCostToBe - 1)
                        .sorted((e1, e2) -> e2.getCost(self) - e1.getCost(self)).collect(Collectors.toList());

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

            List<Card> filtered = candidates.stream().filter(c -> c.getCost(self) <= self.getHands().size() - 1)
                    .sorted((e1, e2) -> e2.getCost(self) - e1.getCost(self)).collect(Collectors.toList());

            if (filtered.size() == 0)
                return 0;

            System.out.println(filtered);
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
        if (commodities.size() != 0) {
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

    private int getWorkers() {
        return this.self.getWorkers().size();
    }

    private int getGDP() {
        return GameManager.getInstance().getGDP();
    }

    // #endregion
    private class WeightedCardValue {
        public WeightedCardValue(CardCategory category) {
            this.category = category;
        }

        public CardCategory category;
        public int score = 0;
    }
}
