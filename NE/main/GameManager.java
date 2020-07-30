package NE.main;

import java.util.ArrayList;
import java.util.List;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.AIPlayer;
import NE.player.HumanPlayer;
import NE.player.Player;
import NE.player.RandomAI;
import NE.player.TAI;
import NE.player.Worker;

public class GameManager {

    private int maxTurn = 9;
    private int currentTurn = 1;
    private int cardsInDeck = 40;
    private boolean isSinglePlay;
    private Board board;
    private List<Player> players = new ArrayList<>();
    private Player parentPlayer;

    private static GameManager theInstance;

    private GameManager() {
        this.board = new Board(this.cardsInDeck);
        this.players.add(new HumanPlayer(this.board));
        // this.players.add(new AIPlayer(new TAI()));
        this.players.add(new AIPlayer(this.board, new RandomAI()));
        this.players.add(new AIPlayer(this.board, new RandomAI()));

        // playerにカードを配る TODO

        this.isSinglePlay = this.players.size() == 1;

        this.parentPlayer = this.players.get(0);

    }

    public static GameManager getInstance() {
        if (theInstance == null) {
            System.out.println("GM Instantiated...");
            theInstance = new GameManager();
        }
        return theInstance;
    }

    public void run() {

        // ゲーム開始

        // ターンのループ
        while (this.currentTurn <= this.maxTurn) {

            // ターン開始
            System.out.println();
            System.out.println("Turn: " + this.currentTurn);
            System.out.println();

            // 公共の建物を建てる TODO

            // 全てのフィールド上のカードを労働可能にする
            this.board.unbanAll();

            for (Player player : this.players) {
                // 個人所有の建物を労働可能にする
                player.unbanAll();
                // 全プレイやーの労働者を労働可能にする
                player.refreshWorkers();
            }

            // 親プレイヤーから始める
            Player currentPlayer = this.parentPlayer;

            while (true) {
                // playerリストをループ
                // まだ行動可能な労働者を保持していたらそのプレイヤーの番になる
                // 全員の労働者が行動不可ならターン終了

                // そのプレイヤーに労働可能な労働者がいるかチェック
                Worker worker = null;
                if ((worker = currentPlayer.getWorkableWorker()) == null) {
                    currentPlayer = getNextPlayer(currentPlayer);
                    continue;
                }

                // ターンが回ってきたプレイヤーの情報を表示
                System.out.println();
                System.out.println("Name: " + currentPlayer.getName());
                System.out.println();
                System.out.println("GDP: " + this.board.getGdp());
                System.out.println("Score: " + currentPlayer.getScore());
                System.out.println("Money: " + currentPlayer.getMoney());
                System.out.println("Hands: " + currentPlayer.getHands());
                // 選択肢を表示
                System.out.println("1 => 公共フィールド");
                System.out.println(this.board.getBuildings());
                System.out.println("2 => 自フィールド");
                System.out.println(currentPlayer.getBuildings());

                if (currentPlayer instanceof HumanPlayer) {
                    humanPlayerTurn(currentPlayer);
                } else {
                    AIPlayerTurn(currentPlayer);
                }

                // 働き済みにする
                worker.setAlreadyWorked(true);

                // シングルプレイの場合は、フィールド上のカードの一番新しくかつ労働可能なカードを一枚取得し、労働不可にする
                if (isSinglePlay) {
                    this.board.ban();
                }

                // ターンを次のプレイヤーへ
                currentPlayer = getNextPlayer(currentPlayer);

                // 全プレイヤーの労働者がすでに労働しているならbreak
                // このやり方は毎ターン二重ループを回して判定しているので重い処理
                // 別の方法としては、上のcontinueがプレイヤー数以上続いたらbreakするという方法もある。
                if (isAllWorkersAlreadyWorked())
                    break;
            }

            // 以下は全員の行動が終了したとき

            // 最初に支払処理をするプレイヤーは親プレイヤー
            Player currentlyPayingPlayer = this.parentPlayer;

            // ターン終了処理
            int count = 0;
            while (count < this.players.size()) {
                pay(currentlyPayingPlayer);
                currentlyPayingPlayer = getNextPlayer(currentlyPayingPlayer);
                count++;
            }

            this.currentTurn++;

        }

        // ゲーム終了処理
        // 所持物件の価値を計算する
        // 最終スコアを計算する

    }

    private boolean isAllWorkersAlreadyWorked() {
        boolean isAllWorkersAlreadyWorked = true;
        // TODO stream
        for (Player player : this.players) {
            for (Worker worker : player.getWorkers()) {
                if (!worker.isAlreadyWorked())
                    return false;
            }
        }
        return isAllWorkersAlreadyWorked;
    }

    private void pay(Player player) {
        // 労働者一人当たりの賃金
        int wages;
        if (this.currentTurn <= 2) {
            wages = 3;
        } else if (this.currentTurn <= 4) {
            wages = 4;
        } else if (this.currentTurn <= 6) {
            wages = 5;
        } else {
            wages = 6;
        }

        // トータル賃金
        int totalWages = player.getWorkers().size() * wages;

        // 賃金を所持金でまかなえなければ所持物件を売る
        // 売れない物件はまだ未実装
        while (player.getMoney() < totalWages && player.getBuildings().size() > 0) {
            if (player instanceof HumanPlayer) {
                System.out.println("売却するカードを選択してください");
                Display.printChoices(player.getBuildings());
                int option = Display.scanNextInt(player.getBuildings().size());
                player.sellBuildings(this.board, option);
            } else {
                AIPlayer ai = (AIPlayer) player;
                int option = ai.getBrain().sell(ai, this.board);
                ai.sellBuildings(this.board, option);
            }
        }
        // 賃金を支払う
        player.payMoney(this.board, totalWages);
    }

    private Player getNextPlayer(Player currentPlayer) {
        int index = this.players.indexOf(currentPlayer);
        index++;
        if (index >= this.players.size()) {
            index = 0;
        }
        return this.players.get(index);
    }

    private void humanPlayerTurn(Player currentPlayer) {
        boolean done = false;
        do {
            // 労働者を働かせるエリアを選ぶ。まず二択
            int optionA = Display.scanNextInt(2);
            List<Card> area = new ArrayList<>();
            switch (optionA) {
                case 0:
                    area = this.board.getBuildings();
                    Display.printChoices(this.board.getBuildings());
                    break;
                case 1:
                    area = currentPlayer.getBuildings();
                    Display.printChoices(currentPlayer.getBuildings());
                    break;
            }

            if (area.size() == 0) {
                System.out.println("選択可能なカードが存在しません");
                continue;
            }

            // 具体的なカードの選択を受け取る
            int optionB = Display.scanNextInt(area.size());

            // 選択されたカードを取得
            Card card = this.board.getBuildings().get(optionB);
            // カードに応じて、使用したときの選択肢の入力を求めリストとして受け取る
            List<Integer> options = card.promptChoice(currentPlayer, this.board);

            // カードを使用する
            done = card.work(currentPlayer, this.board, options);
        } while (!done);
    }

    private void AIPlayerTurn(Player currentPlayer) {

        AIPlayer player = (AIPlayer) currentPlayer;
        boolean done = false;
        int stucked = -1;
        do {
            stucked++;
            // Humanplyaerの時と違って、どのフィールドで、どのカードを使うかも含めてreturnしてくる
            List<Integer> options = player.getBrain().think(player, this.board, stucked);
            System.out.println(options);

            int areaChoice = options.remove(0);
            List<Card> area;
            switch (areaChoice) {
                case 0:
                    area = this.board.getBuildings();
                    Display.printChoices(this.board.getBuildings());
                    break;
                case 1:
                    area = currentPlayer.getBuildings();
                    Display.printChoices(currentPlayer.getBuildings());
                    break;
                default:
                    area = new ArrayList<>();
            }

            if (area.size() == 0) {
                continue;
            }

            // 選択されたフィールドから選択されたカードを取得
            Card card = area.get(options.remove(0));

            System.out.println(options);
            System.out.println(card);

            // カードを使用する
            done = card.work(player, this.board, options);

            System.out.println(done);
            stucked++;
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }

            if (stucked >= 3) {
                // 鉱山を使わせる
                // TODO
                done = this.board.getBuildings().get(0).work(player, this.board, options);

            }

        } while (!done);
    }

    public int getMaxTurn() {
        return maxTurn;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public boolean isSinglePlay() {
        return isSinglePlay;
    }

    public void setSinglePlay(boolean isSinglePlay) {
        this.isSinglePlay = isSinglePlay;
    }

    public void refreshDeck() {
        System.out.println("deck refreshed!");
        System.out.println(this.board.getTrash().size());
        while (this.board.getTrash().size() > 0) {
            System.out.println("card added!");
            this.board.getDeck().addCard(this.board.getTrash().remove(0));
            System.out.println(this.board.getDeck().getCards().size());
        }
    }

}