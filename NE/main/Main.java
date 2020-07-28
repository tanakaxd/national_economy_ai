package NE.main;

import NE.board.Board;
import NE.board.Deck;
import NE.display.Display;
import NE.player.Player;

public class Main {
    public static void main(String[] args) {
        // 初期化セットアップ
        // int maxTurns = 2;
        // int turns = 1;
        // int deckCards = 20;
        // Board board = new Board();
        // Deck deck = new Deck(deckCards);
        // Player player = new Player();

        GameManager gameManager = new GameManager();
        gameManager.init();
        gameManager.run();

    }

}
