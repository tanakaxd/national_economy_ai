package NE.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.card.market.MarketCardA;
import NE.card.market.MarketCardB;
import NE.card.market.MarketCardC;
import NE.card.market.MarketCardD;
import NE.card.market.MarketCardE;
import NE.card.school.SchoolCardB;
import NE.card.school.SchoolCardC;
import NE.card.school.SchoolCardD;
import NE.display.Display;
import NE.player.AIPlayer;
import NE.player.HumanPlayer;
import NE.player.Player;
import NE.player.Worker;
import NE.player.ai.SimpleTAI;
import NE.player.ai.tai.TAI;
import NE.player.ai.tai.TAIGeneExtractor;
import NE.player.ai.tai.TAIGeneLoader.GeneMode;

public class GameManager {

    private static GameManager theInstance;
    private Board board;
    private List<Player> players = new ArrayList<>();
    private Player parentPlayer;
    // private Player currentPlayer;
    private int currentTurn = 1;
    private int currentWage;
    private List<Card> publicBuildings = new ArrayList<>();

    // game settings
    private int maxTurn = 9;
    private int cardsInDeck = 40;// when random
    private boolean isSinglePlay;
    private boolean hasManyPlayers;// 3人以上
    private boolean isRandomDeck = false;

    // AI settings
    private boolean isAITransparent = false;
    private boolean pauseOnAITurn = false;
    private int waitTime = 0;
    private int maxStucks = 10;

    private GameManager() {

    }

    public void init() {
        // TODO ボードは初期化にプレイヤー人数を必要として、プレイヤーはボードからのドローが必要
        // ボードが持つデッキはGMのRandomInitフィールドに依存している

        // この時点でボートとデッキが作られる
        // 公共職場はまだ
        this.board = new Board(this.cardsInDeck);

        // this.players.add(new HumanPlayer(this.board));

        // カード売買テスト
        // Player player = this.players.get(0);
        // player.build(new AgricultureCardA());
        // player.build(new AgricultureCardB());
        // player.build(new AgricultureCardC());
        // player.build(new AgricultureCardA());
        // player.build(new FacilityCardB());
        // player.addWorker(false);
        // player.addWorker(false);
        // player.addWorker(false);
        // player.addWorker(false);

        // this.players.add(new AIPlayer(this.board, new AhoAI()));
        this.players.add(new AIPlayer(this.board, new SimpleTAI()));
        this.players.add(new AIPlayer(this.board, new TAI(GeneMode.GENETIC_ALGORITHM)));
        this.players.add(new AIPlayer(this.board, new TAI()));
        this.players.add(new AIPlayer(this.board, new TAI()));
        // this.players.add(new AIPlayer(this.board, new TAI()));
        // this.players.add(new AIPlayer(this.board, new TAI(132, 40, 99, 125, 29,
        // 73)));
        // this.players.add(new AIPlayer(this.board, new
        // TAI(GeneMode.GENETIC_ALGORITHM)));
        // this.players.add(new AIPlayer(this.board, new TAI(GeneMode.SPECIFIC)));
        // this.players.add(new AIPlayer(this.board, new TAI()));
        // this.players.add(new AIPlayer(this.board, new RandomAI()));
        // this.players.add(new AIPlayer(this.board, new SimpleTAI()));
        // this.players.add(new AIPlayer(this.board, new RandomAI()));
        // this.players.add(new AIPlayer(this.board, new RandomAI()));
        // this.players.add(new AIPlayer(this.board, new RandomAI()));

        this.publicBuildings.add(new MarketCardA());
        this.publicBuildings.add(new MarketCardB());
        this.publicBuildings.add(new SchoolCardB());
        this.publicBuildings.add(new MarketCardC());
        this.publicBuildings.add(new SchoolCardC());
        this.publicBuildings.add(new MarketCardD());
        this.publicBuildings.add(new SchoolCardD());
        this.publicBuildings.add(new MarketCardE());

        // playerにカードを配る TODO
        // TODO single playの時のデッキ補正。山札の一番上が食品工場

        this.isSinglePlay = this.players.size() == 1;

        this.hasManyPlayers = this.players.size() >= 3;

        this.parentPlayer = this.players.get(0);
        System.out.println("GM INITIALISED...");

    }

    public void run() {

        // ゲーム開始

        // ターンのループ
        while (this.currentTurn <= this.maxTurn) {

            initTurn();

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
                displayPlayerInfo(currentPlayer);

                // カード使用のメインループ
                currentPlayer.processTurn(board);

                // 働き済みにする
                worker.setWorkable(false);
                currentPlayer.acted();

                // シングルプレイの場合は、フィールド上のカードの一番新しくかつ労働可能なカードを一枚取得し、労働不可にする
                if (isSinglePlay) {
                    this.board.ban();
                }

                // ターンを次のプレイヤーへ
                currentPlayer = getNextPlayer(currentPlayer);

                // 全プレイヤーの全労働者がすでに労働しているならbreak
                // このやり方は毎ターン二重ループを回して判定しているので重い処理
                // 別の方法としては、上のcontinueがプレイヤー数以上続いたらbreakするという方法もある。
                if (isAllPlayersDone())
                    break;
            }
            // 以下は全員の行動が終了したとき
            // ターン終了処理
            terminateTurn();
        }
        // ゲーム終了処理
        finishGame();
    }

    public void initBoardBuildings() {
        this.board.initBoardBuildings();
    }

    private void displayPlayerInfo(Player currentPlayer) {
        System.out.println();
        System.out.println("CurrentTurn: " + this.currentTurn);
        System.out.println("DeckCounter: " + this.board.getDeck().getDeckSize());
        System.out.println("TrashCounter: " + this.board.getTrash().size());
        System.out.println();
        System.out.println("--------------PLAYER INFO----------------");
        System.out.println("Name: " + currentPlayer.getName());
        System.out.println();
        System.out.println("ActionCounts: " + currentPlayer.getActionCount());
        System.out.println("OwnedWorkers: " + currentPlayer.getWorkers().size());
        System.out.println("HoldholdIncome: " + this.board.getHoldholdIncome());
        System.out.println("ExpectedWages: " + currentPlayer.getWorkers().size() * this.currentWage);
        System.out.println("Money: " + currentPlayer.getMoney());
        System.out.println("Debt: " + -currentPlayer.getDebt() + " => " + currentPlayer.getDebt() * 3);
        System.out.println("Score: " + currentPlayer.getScore());
        System.out.println(
                "VictoryPoints: " + currentPlayer.getVictoryPoint() + " => " + currentPlayer.calcVictoryPointsScore());
        System.out.println("HandsCounter: " + currentPlayer.getHands().size());
        System.out.println("Hands: " + currentPlayer.getHands());
        System.out.println("public Buildings: " + this.board.getBuildings());
        System.out.println("Owned Buildings: " + currentPlayer.getBuildings());
        System.out.println("History: " + currentPlayer.getHistory());
        System.out.println("-------------------------");

    }

    private void initTurn() {

        System.out.println();
        System.out.println("Turn: " + this.currentTurn);
        System.out.println();

        // 賃金上昇処理
        inflateWage();

        // 全てのフィールド上のカードを労働可能にする
        this.board.refreshAllBuildings();

        // 全プレイヤー、ターン開始準備
        for (Player player : this.players) {
            player.initForTurn();
        }
    }

    private void inflateWage() {
        if (this.currentTurn <= 2) {
            this.currentWage = 2;
        } else if (this.currentTurn <= 5) {
            this.currentWage = 3;
        } else if (this.currentTurn <= 7) {
            this.currentWage = 4;
        } else {
            this.currentWage = 5;
        }
    }

    private void terminateTurn() {

        // 最初に支払処理をするプレイヤーは親プレイヤー
        Player currentPlayer = this.parentPlayer;

        int count = 0;
        while (count < this.players.size()) {
            // 賃金支払処理
            currentPlayer.payWages(board);
            // 手札調整処理
            capHands(currentPlayer);
            currentPlayer = getNextPlayer(currentPlayer);
            count++;
        }

        if (this.publicBuildings.size() != 0) {
            this.board.getBuildings().add(this.publicBuildings.remove(0));
        }

        this.currentTurn++;
    }

    private void capHands(Player currentPlayer) {
        List<Card> hands = currentPlayer.getHands();
        if (hands.size() <= currentPlayer.getHandLimits())
            return;

        System.out.println(currentPlayer.getName());
        System.out.println("手札上限を超える分を捨ててください");
        Display.printChoices(hands);
        int discardsAmount = hands.size() - currentPlayer.getHandLimits();

        List<Integer> indexesToDiscard = currentPlayer.askDiscard(board, discardsAmount);

        for (Integer integer : indexesToDiscard) {
            currentPlayer.discard(board, integer);
        }
        // 不適切な値なら強制的にindex0のカードを捨てさせる
        // currentPlayer.discard(board, ai.getHands().get(0));

    }

    private void finishGame() {

        List<Player> rankings = this.players.stream().sorted(Comparator.comparing(Player::getScore).reversed())
                .collect(Collectors.toList());

        // 最終スコアを計算する
        System.out.println();
        System.out.println("Game Finished Successfully");
        System.out.println("HERE IS RANKING");
        System.out.println();
        for (int i = 0; i < rankings.size(); i++) {
            int rank = i + 1;
            System.out.println(rank + ": " + rankings.get(i).getName() + " score=" + rankings.get(i).getScore());
        }

        // TODO TAI用 データを送る
        if (Main.IS_TAI_RECORDING)
            TAIGeneExtractor.getInstance().terminate(rankings);
    }

    private boolean isAllPlayersDone() {
        boolean isAllPlayersDone = true;
        for (Player player : this.players) {
            if (player.isActive())
                return false;
        }
        return isAllPlayersDone;
    }

    private Player getNextPlayer(Player currentPlayer) {
        int index = this.players.indexOf(currentPlayer);
        index++;
        if (index >= this.players.size()) {
            index = 0;
        }
        return this.players.get(index);
    }

    // #region setter/getter
    public static boolean isAITransparent() {
        return getInstance().isAITransparent;
    }

    public static GameManager getInstance() {
        if (theInstance == null) {
            System.out.println("GM INSTANTIATED...");
            theInstance = new GameManager();
        }
        return theInstance;
    }

    public int getMaxTurn() {
        return maxTurn;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Player getParentPlayer() {
        return parentPlayer;
    }

    public boolean isSinglePlay() {
        return isSinglePlay;
    }

    public void setParentPlayer(Player parentPlayer) {
        this.parentPlayer = parentPlayer;
    }

    public int getHouseholdIncome() {
        return this.board.getHoldholdIncome();
    }

    public boolean isRandomDeck() {
        return this.isRandomDeck;
    }

    public boolean hasManyPlayers() {
        return this.hasManyPlayers;
    }

    public static int getMaxStucks() {
        return getInstance().maxStucks;
    }

    public static int getWaitTime() {
        return getInstance().waitTime;
    }

    public static boolean isPauseOnAITurn() {
        return getInstance().pauseOnAITurn;
    }

    public static int getCurrentWage() {
        return getInstance().currentWage;
    }
    // #endregion

}