package NE.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.card.agriculture.AgricultureLesser;
import NE.card.industry.IndustryLesser;
import NE.card.industry.IndustryMedium;

public abstract class Player {
    protected List<Card> hands = new LinkedList<>();
    protected List<Card> buildings = new ArrayList<>();

    protected int workersCount = 2;
    protected int money = 5;
    protected int handLimits = 5;
    protected int workersLimits = 5;

    public Player() {
        this.hands.add(new AgricultureLesser());
        this.hands.add(new AgricultureLesser());
        this.hands.add(new AgricultureLesser());
        this.hands.add(new IndustryLesser());
        this.hands.add(new IndustryMedium());
    }

    public void discard(Board board, int index) {
        board.getTrash().add(this.hands.remove(index));
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
        this.hands.add(board.getDeck().draw());
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

    public boolean addWorker() {
        if (this.workersCount >= workersLimits)
            return false;

        this.workersCount++;
        return true;

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

    public void unbanAll() {
        for (Card card : buildings) {
            card.setWorked(false);
        }
    }

}
