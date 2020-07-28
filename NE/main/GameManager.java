package NE.main;

import NE.board.Board;
import NE.board.Deck;
import NE.display.Display;
import NE.player.HumanPlayer;
import NE.player.Player;

public class GameManager {

    private int maxTurns = 2;
    private int turns = 1;
    private int deckCards = 20;
    private Board board;
    private Player player;

    public GameManager() {
        board = new Board(deckCards);
        player = new HumanPlayer();

    }

    public void run() {

        // ターン開始
        for (int i = 0; i < maxTurns; i++) {
            System.out.println("Turn: " + turns);
            // 全てのフィールド上のカードを労働可能にする
            board.unbanAll();
            player.unbanAll();// TODO

            for (int j = 0; j < player.getWorkersCount(); j++) {

                if (player instanceof HumanPlayer) {
                    boolean done = false;
                    do {
                        // 労働者を働かせる場所を選ぶ
                        // 手札を表示
                        System.out.println(player.getHands());
                        // 所持金を表示
                        // 選択肢を表示
                        Display.printChoices(board.getBuildings());
                        Display.printChoices(player.getBuildings());
                        // System.out.println(field.getBuildings());
                        // System.out.println(player.getBuildings());
                        // 選択を受け取る
                        // 可能な選択でなければループ

                        int option = Display.scanNextInt(3);
                        // カードの効果を発揮
                        // done = field.workOnCard(option, deck, player);
                        // board.workOnCard(option, deck, player);
                        done = workOnCard(option);

                        // done = true;

                    } while (!done);

                } else {

                }

                // シングルプレイの場合は、フィールド上のカードの一番新しくかつ労働可能なカードを一枚取得し、労働不可にする
                // board.ban();

            }

            // ターン終了処理
            // 賃金を所持金でまかなえなければ所持物件を売る
            // 賃金を支払う
            turns++;

        }

        // 終了処理
        // 所持物件の価値を計算する
        // 最終スコアを計算する

    }

    private boolean workOnCard(int option) {

        return false;
    }

    public void init() {

    }

}