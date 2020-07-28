package NE.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import NE.card.AgricultureCard;
import NE.card.Card;

public class Player {
    private List<Card> hands = new LinkedList<>();
    // private List<Integer> hands = new LinkedList<>();
    private List<Card> buildings = new ArrayList<>();

    private int workersCount = 2;
    private int money = 5;
    private int handLimits = 5;

    public Player() {
        this.hands.add(new AgricultureCard());
        this.hands.add(new AgricultureCard());
        this.hands.add(new AgricultureCard());
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

    public void unbanAll() {
        for (Card card : buildings) {
            card.setWorked(false);
        }
    }

}
