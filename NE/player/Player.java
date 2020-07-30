package NE.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;

public abstract class Player {
    protected List<Card> hands = new LinkedList<>();
    protected List<Card> buildings = new ArrayList<>();

    // TODO trashとdeck

    protected List<Worker> workers = new ArrayList<>();
    protected int initialWorkers = 2;
    protected int workersCount = 2;
    protected int money = 5;
    protected int debt = 0;
    protected int score = 0;
    protected int handLimits = 5;
    protected int initialHands = 3;
    protected int workersLimits = 5;
    protected int actionCount = 1;
    protected String name;

    public Player(Board board) {
        for (int i = 0; i < initialHands; i++) {
            draw(board);
        }

        for (int i = 0; i < this.initialWorkers; i++) {
            workers.add(new Worker());
        }

        // TODO
        // trash等の参照を取得する
        // playerとboardの結びつきはかなり強い。両者とも他方の存在なしには意味を失う。
        // つまり、compositionが妥当。

    }

    // public void discard(Board board, int index) {//
    // 一つずつindexをもらうとremoveでずれてoutofboundsになる
    // board.getTrash().add(this.hands.remove(index));// TODO
    // System.out.println("discard");
    // System.out.println(board.getTrash());
    // }
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
        System.out.println("discard");
        System.out.println(board.getTrash());

        return true;
    }

    public void discard(Board board, Card cardToDiscard) {
        board.getTrash().add(cardToDiscard);
        this.hands.remove(cardToDiscard);
        System.out.println("discard");
        System.out.println(board.getTrash());
    }

    public void build(int index) {
        this.buildings.add(this.hands.remove(index));
    }

    public void build(Card cardToBuild) {
        this.buildings.add(cardToBuild);
        this.hands.remove(cardToBuild);
    }

    public void draw(Board board) {
        this.hands.add(board.getDeck().draw());
    }

    public boolean isActive() {
        for (Worker worker : this.workers) {
            if (worker.isWorkable())
                return true;
        }
        return false;
    }

    public void sellBuildings(Board board, Card cardToSell) {
        this.money += cardToSell.getValue();
        board.getBuildings().add(cardToSell);
        this.buildings.remove(cardToSell);
    }

    public void sellBuildings(Board board, int index) {
        // TODO singlePlay
        this.money += this.buildings.get(index).getValue();
        board.getBuildings().add(this.buildings.remove(index));
    }

    public boolean addWorker(boolean isWorkable) {
        if (this.workers.size() >= workersLimits)
            return false;
        this.workers.add(new Worker(isWorkable));
        return true;
    }

    public void unbanAll() {
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
        System.out.println("payment:" + payment);
        board.addGdp(payment);
        this.money -= totalWages;
        if (money < 0) {
            this.debt += this.money;
            this.money = 0;
        }
    }

    public void calcScore() {
        int totalRealEstateValue = 0;
        for (Card card : buildings) {
            totalRealEstateValue += card.getValue();
        }
        this.score = totalRealEstateValue + this.money + (this.debt * 3);
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
        unbanAll();
        // 全プレイやーの労働者を労働可能にする
        refreshWorkers();
        // 行動回数カウンターをリセットする
        this.actionCount = 1;
    }

    public void acted() {
        this.actionCount++;
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

    public int getWorkersCount() {
        return workersCount;
    }

    public void setWorkersCount(int workersCount) {
        this.workersCount = workersCount;
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

    // #endregion
}
