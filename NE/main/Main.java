package NE.main;

import NE.board.Board;
import NE.board.Deck;
import NE.display.Display;
import NE.player.Player;

public class Main {
    public static void main(String[] args) {
        GameManager.getInstance().init();
        GameManager.getInstance().run();
    }
}
