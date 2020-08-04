package NE;

import NE.board.Board;
import NE.card.construction.ConstructionCardE;
import NE.card.facility.FacilityCardA;
import NE.card.facility.FacilityCardB;
import NE.card.facility.FacilityCardD;
import NE.card.facility.FacilityCardH;
import NE.card.market.MarketCardG;
import NE.card.market.MarketCardI;
import NE.player.HumanPlayer;
import NE.player.Player;

public class Debug {
    public static void main(String[] args) {
        Player p = new HumanPlayer(new Board(10));
        p.build(new FacilityCardH());
        p.build(new FacilityCardD());
        p.build(new FacilityCardA());
        p.build(new FacilityCardB());
        p.build(new MarketCardI());
        p.build(new MarketCardG());
        p.build(new ConstructionCardE());
        p.build(new ConstructionCardE());
        System.out.println(p.getHands());
        System.out.println(p.getScore());

    }
}