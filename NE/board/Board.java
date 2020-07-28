package NE.board;

import java.util.ArrayList;
import java.util.List;

import NE.card.AgricultureCard;
import NE.card.Card;
import NE.card.DrawCard;
import NE.card.SchoolCard;
import NE.player.Player;

public class Board {
    private Deck deck;
    private List<Card> buildings = new ArrayList<>();
    private int gdp = 0;

    public Board(int deckcards) {
        this.deck = new Deck(deckcards);
        this.buildings.add(new AgricultureCard(0, "agri", 0, 0));
        this.buildings.add(new DrawCard(0, "draw", 0, 0));
        this.buildings.add(new SchoolCard(0, "school", 0, 0));
        // this.buildings.add(new BuilderCard(0, "builder", 0, 0));

    }

    public void workOnCard(int option, Deck deck, Player player) {
        this.buildings.get(option).work(player, this, deck);
    }

    public void ban() {
        for (int i = this.buildings.size() - 1; i >= 0; i--) {
            if (this.buildings.get(i).isWorked()) {
                continue;
            } else {
                this.buildings.get(i).setWorked(true);
                break;
            }
        }
    }

    public List<Card> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Card> buildings) {
        this.buildings = buildings;
    }

    public int getGdp() {
        return gdp;
    }

    public void setGdp(int gdp) {
        this.gdp = gdp;
    }

    public void unbanAll() {
        for (Card card : buildings) {
            card.setWorked(false);
        }
    }

}
