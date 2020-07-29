package NE.main;

import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.AIPlayer;
import NE.player.HumanPlayer;
import NE.player.Player;
import NE.player.TAI;

public class GameManager {

    private int maxTurn = 2;
    private int currentTurn = 1;
    private int deckCards = 20;
    private Board board;
    private Player player;

    private static GameManager theInstance;

    private GameManager() {
        this.board = new Board(deckCards);
        // this.player = new HumanPlayer();
        this.player = new AIPlayer(new TAI());

    }

    public static GameManager getInstance() {
        if (theInstance == null) {
            theInstance = new GameManager();
        }
        return theInstance;
    }

    public void run() {

        // ターン開始
        for (int i = 0; i < maxTurn; i++) {
            System.out.println("Turn: " + currentTurn);
            // 全てのフィールド上のカードを労働可能にする
            this.board.unbanAll();
            this.player.unbanAll();// TODO

            // playerリストをループ
            // まだ行動可能な労働者を保持していたらそのプレイヤーの番になる
            // 全員の労働者が行動不可ならターン終了

            for (int j = 0; j < this.player.getWorkersCount(); j++) {

                if (this.player instanceof HumanPlayer) {
                    boolean done = false;
                    do {
                        // 手札を表示
                        System.out.println("手札: " + this.player.getHands());
                        // 所持金を表示
                        // 選択肢を表示
                        Display.printChoices(this.board.getBuildings());
                        Display.printChoices(this.player.getBuildings());
                        // System.out.println(field.getBuildings());
                        // System.out.println(player.getBuildings());

                        // 労働者を働かせる場所を選ぶ
                        // 選択を受け取る
                        int option = Display.scanNextInt(this.board.getBuildings().size());

                        // 選択されたカードを取得
                        Card card = board.getBuildings().get(option);
                        // カードに応じて、使用したときの選択肢をリストとして受け取る
                        List<Integer> options = card.promptChoice(this.player, this.board);

                        // カードを使用する
                        done = card.work(this.player, this.board, options);

                    } while (!done);
                } else {
                    // 手札を表示
                    System.out.println("手札: " + this.player.getHands());
                    // System.out.println(this.player);
                    AIPlayer player = (AIPlayer) this.player;
                    // System.out.println(this.player);
                    // System.out.println(player == this.player);
                    boolean done = false;
                    int stucked = 0;
                    do {
                        Display.printChoices(this.board.getBuildings());
                        Display.printChoices(this.player.getBuildings());

                        // Humanplyaerの時と違って、どのカードを使うかも含めてreturnしてくる
                        List<Integer> options = player.getBrain().think(player, this.board, stucked);

                        System.out.println(options);

                        // 選択されたカードを取得
                        Card card = this.board.getBuildings().get(options.remove(0));

                        System.out.println(options);

                        System.out.println(card);

                        // 使用するカード選択のための数字をリストから除外
                        // List<Integer> modifiedOptions =
                        // options.stream().skip(1).collect(Collectors.toList());

                        // カードを使用する
                        done = card.work(player, this.board, options);

                        System.out.println(done);

                        stucked++;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (stucked >= 3) {
                            // 鉱山を使わせる
                            // TODO
                            done = this.board.getBuildings().get(0).work(player, board, options);

                        }

                    } while (!done);
                }

                // シングルプレイの場合は、フィールド上のカードの一番新しくかつ労働可能なカードを一枚取得し、労働不可にする
                // if (players.size() == 1) {
                // // board.ban();
                // }

            }

            // ターン終了処理
            // 賃金を所持金でまかなえなければ所持物件を売る
            // 賃金を支払う
            currentTurn++;

        }

        // 終了処理
        // 所持物件の価値を計算する
        // 最終スコアを計算する

    }

    public int getMaxTurn() {
        return maxTurn;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

}