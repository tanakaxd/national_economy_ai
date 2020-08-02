package NE.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import NE.board.Board;
import NE.card.Card;
import NE.card.CommodityCard;
import NE.card.Card.CardCategory;
import NE.card.facility.FacilityCard;
import NE.main.GameManager;

public abstract class Player {

    protected List<Card> hands = new LinkedList<>();
    protected List<Card> buildings = new ArrayList<>();
    protected List<Worker> workers = new ArrayList<>();
    protected List<Card> buildingsToWorkThisTurn = new ArrayList<>();
    // TODO trashとdeck
    // protected List<Card> trash;

    protected String name;
    protected int initialWorkers = 2;
    protected int initialHands = 3;
    protected int money;
    protected int debt = 0;
    protected int victoryPoint = 0;
    protected int score = 0;
    protected int handLimits = 5;
    protected int workersLimits = 5;
    protected int actionCount = 1;

    protected boolean useWinery = false;
    protected boolean useMine = false;

    protected static int initialMoney = 5;

    public Player(Board board) {
        this.money = initialMoney;
        initialMoney++;

        for (int i = 0; i < this.initialHands; i++) {
            draw(board);
        }

        for (int i = 0; i < this.initialWorkers; i++) {
            workers.add(new Worker());
        }

        // TODO
        // trash等の参照を取得する
        // playerとboardの結びつきはかなり強い。両者とも他方の存在なしには意味を失う。
        // compositionが妥当かもしれない

    }

    // 捨てたいカードを聞く。人間なら入力を求め、AIならthinkDiscard()を使う。
    // ただし、askBuildと別に聞いているため、建てるカードと捨てるカードが被る可能性が出てくることに注意
    public abstract List<Integer> askDiscard(Board board, int cost);

    // overloadでかぶりを禁止するメソッドも作った。例えば、建築系ではこっちを使いたい。
    // 上を消してこっちに統一する方法もあるが、必要ない場合にいちいちnullを代入することになる
    public abstract List<Integer> askDiscard(Board board, int cost, List<Integer> indexesNotAllowed);

    public abstract List<Integer> askBuild(Board board, int amounts, Card card);

    // 実際に捨てる処理は不可能な場合があるため、聞く処理と分ける必要がある
    public void discard(Board board, int index) {
        try {
            board.getTrash().add(this.hands.remove(index));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            board.getTrash().add(this.hands.remove(0));
        }
    }

    public void discard(Board board, Card cardToDiscard) {
        board.getTrash().add(cardToDiscard);
        this.hands.remove(cardToDiscard);
    }

    public void build(int index) {
        this.buildings.add(this.hands.remove(index));
    }

    public void build(Card cardToBuild) {
        this.buildings.add(cardToBuild);
        this.hands.remove(cardToBuild);
    }

    public void draw(Board board) {
        this.hands.add(board.draw());
    }

    public boolean isActive() {
        for (Worker worker : this.workers) {
            if (worker.isWorkable())
                return true;
        }
        return false;
    }

    public void sellBuildings(Board board, Card cardToSell) {
        if (cardToSell.getCategory() == CardCategory.FACILITY)
            return;
        if (GameManager.getInstance().isSinglePlay()) {
            this.money += cardToSell.getValue() / 2;
        } else {
            this.money += cardToSell.getValue();
        }
        board.getBuildings().add(cardToSell);
        this.buildings.remove(cardToSell);
    }

    public void sellBuildings(Board board, int index) {
        Card cardToSell = this.buildings.get(index);
        if (cardToSell.getCategory() == CardCategory.FACILITY)
            return;
        if (GameManager.getInstance().isSinglePlay()) {
            this.money += cardToSell.getValue() / 2;
        } else {
            this.money += cardToSell.getValue();
        }
        board.getBuildings().add(this.buildings.remove(index));
    }

    public boolean addWorker(boolean isWorkable) {
        if (this.workers.size() >= workersLimits)
            return false;
        this.workers.add(new Worker(isWorkable));
        return true;
    }

    public void refreshBuildings() {
        for (Card card : this.buildings) {
            card.setWorked(false);
        }
    }

    public void refreshWorkers() {
        for (Worker worker : this.workers) {
            worker.setWorkable(true);
        }
    }

    public void payMoney(Board board, int totalWages) {
        int payment = Math.min(this.money, totalWages);
        System.out.println(this.name + " payed: $" + payment);
        board.addGdp(payment);
        this.money -= totalWages;
        if (this.money < 0) {
            this.debt += this.money;
            this.money = 0;
        }
    }

    public void calcScore() {
        int totalRealEstateValue = 0;
        for (Card card : buildings) {
            totalRealEstateValue += card.getValue();
        }
        this.score = totalRealEstateValue + this.money + (this.debt * 3) + calcVictoryPointsScore()
                + calcFacilityBonus();
    }

    public int calcVictoryPointsScore() {
        int ex = this.victoryPoint % 3;
        int score = this.victoryPoint / 3 * 10 + ex;
        return score;
    }

    private int calcFacilityBonus() {
        int bonus = this.buildings.stream().filter(c -> c.getCategory() == CardCategory.FACILITY)
                .mapToInt(c -> ((FacilityCard) c).calcBonus(this)).sum();
        return bonus;
    }

    public Worker getWorkableWorker() {
        Worker worker = null;
        for (Worker candidate : this.workers) {
            if (candidate.isWorkable())
                worker = candidate;
        }
        return worker;
    }

    public void earnMoney(Board board, int profit) {
        this.money += profit;
        board.setGdp(board.getGdp() - profit);// TODO
    }

    public void initForTurn() {
        // 個人所有の建物を労働可能にする
        refreshBuildings();
        // 全プレイやーの労働者を労働可能にする
        refreshWorkers();
        // 行動回数カウンターをリセットする
        this.actionCount = 1;
        // 醸造所効果
        if (useWinery) {
            for (int i = 0; i < 4; i++) {
                this.hands.add(new CommodityCard());
            }
            this.useWinery = false;
        }
        this.useMine = false;
    }

    public void acted() {
        this.actionCount++;
    }

    public void addVictoryPoint(int i) {
        this.victoryPoint += i;
    }

    // #region setter&getter

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public int getScore() {
        calcScore();
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInitialHands() {
        return initialHands;
    }

    public void setInitialHands(int initialHands) {
        this.initialHands = initialHands;
    }

    public int getActionCount() {
        return actionCount;
    }

    public void setActionCount(int actionCount) {
        this.actionCount = actionCount;
    }

    public List<Card> getHands() {
        return hands;
    }

    public void setHands(List<Card> hands) {
        this.hands = hands;
    }

    public List<Card> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Card> buildings) {
        this.buildings = buildings;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public int getInitialWorkers() {
        return initialWorkers;
    }

    public void setInitialWorkers(int initialWorkers) {
        this.initialWorkers = initialWorkers;
    }

    public int getHandLimits() {
        return handLimits;
    }

    public void setHandLimits(int handLimits) {
        this.handLimits = handLimits;
    }

    public int getWorkersLimits() {
        return workersLimits;
    }

    public void setWorkersLimits(int workersLimits) {
        this.workersLimits = workersLimits;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public void setVictoryPoint(int victoryPoint) {
        this.victoryPoint = victoryPoint;
    }

    public void useWinery(boolean b) {
        this.useWinery = b;
    }

    public void useMine(boolean b) {
        this.useMine = b;
    }

    public boolean getUseMine() {
        return this.useMine;
    }

    // #endregion
}
