package NE.board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import NE.card.Card;
import NE.card.Card.CardCategory;
import NE.card.construction.ConstructionCardA;
import NE.card.industry.IndustryCardA;
import NE.card.industry.IndustryCardB;
import NE.card.industry.IndustryCardH;
import NE.card.school.SchoolCardA;

public class Board {
    private Deck deck;
    private List<Card> buildings = new ArrayList<>();
    private List<Card> trash = new ArrayList<>();
    private int gdp = 0;

    public Board(int cardsInDeck) {
        this.deck = new Deck(cardsInDeck);
        this.buildings.add(new IndustryCardA());
        this.buildings.add(new ConstructionCardA());
        this.buildings.add(new ConstructionCardA());
        this.buildings.add(new ConstructionCardA());
        this.buildings.add(new SchoolCardA());
        this.buildings.add(new IndustryCardB());
        // this.buildings.add(new IndustryCardH());
        // this.buildings.add(new ConstructionCardE());
    }

    public void refreshDeck() {
        System.out.println("deck refreshed!");
        List<Card> filteredTrash = this.trash.stream().filter(c -> c.getCategory() != CardCategory.COMMODITY)
                .collect(Collectors.toList());
        System.out.println(filteredTrash);
        while (filteredTrash.size() > 0) {
            this.deck.addCard(filteredTrash.remove(0));
        }
        // 破壊的メソッドであることに注意
        // 参照を取得して新しいリストを作っているのでclearしないと、trashにはカードが残ったままでdeck内と重複して存在することになる
        this.trash.clear();
    }

    public Card draw() {
        if (this.deck.getCards().isEmpty()) {
            refreshDeck();
        }
        return this.deck.draw();
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

    public void addGdp(int amount) {
        this.gdp += amount;
    }

    public void refreshAllBuildings() {
        for (Card card : buildings) {
            card.setWorked(false);
        }
    }

    // #region setter&getter

    public List<Card> getBuildings() {
        return buildings;
    }

    public int getGdp() {
        return gdp;
    }

    public void setGdp(int gdp) {
        this.gdp = gdp;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Card> getTrash() {
        return trash;
    }
    // #endregion

}
