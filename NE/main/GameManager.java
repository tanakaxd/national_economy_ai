package NE.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.card.Card.CardCategory;
import NE.card.market.MarketCardA;
import NE.card.market.MarketCardB;
import NE.card.market.MarketCardC;
import NE.card.market.MarketCardD;
import NE.card.market.MarketCardE;
import NE.card.school.SchoolCardB;
import NE.card.school.SchoolCardC;
import NE.card.school.SchoolCardD;
import NE.display.Display;
import NE.player.HumanPlayer;
import NE.player.Player;
import NE.player.Worker;
import NE.player.ai.AIPlayer;
import NE.player.ai.SimpleTAI;
import NE.player.ai.TAI;

public class GameManager {

    private static GameManager theInstance;
    private Board board;
    private List<Player> players = new ArrayList<>();
    private Player parentPlayer;
    // private Player currentPlayer;
    private int currentTurn = 1;
    private int currentWage = 2;
    private List<Card> publicBuildings = new ArrayList<>();

    // game settings
    private int maxTurn = 9;
    private int cardsInDeck = 40;// when random
    private boolean isSinglePlay;
    private boolean hasManyPlayers;// 3人以上
    private boolean isRandomDeck = false;

    // AI settings
    private boolean isAITransparent = true;
    private int waitTime = 0;
    private int maxStucks = 20;

    private GameManager() {

    }

    public void init() {
        this.board = new Board(this.cardsInDeck);

        // this.players.add(new HumanPlayer(this.board));
        this.players.add(new AIPlayer(this.board, new TAI()));
        // this.players.add(new AIPlayer(this.board, new RandomAI()));
        this.players.add(new AIPlayer(this.board, new SimpleTAI()));
        this.players.add(new AIPlayer(this.board, new SimpleTAI()));
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

                if (currentPlayer instanceof HumanPlayer) {
                    humanPlayerTurn(currentPlayer);
                } else {
                    AIPlayerTurn(currentPlayer);
                }

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
        System.out.println("GDP: " + this.board.getGdp());
        System.out.println("ExpectedWages: " + currentPlayer.getWorkers().size() * this.currentWage);
        System.out.println("Money: " + currentPlayer.getMoney());
        System.out.println("Debt: " + -currentPlayer.getDebt() + " => " + currentPlayer.getDebt() * 3);
        System.out.println("Score: " + currentPlayer.getScore());
        System.out.println(
                "VictoryPoints: " + currentPlayer.getVictoryPoint() + " => " + currentPlayer.calcVictoryPointsScore());
        System.out.println("HandsCounter: " + currentPlayer.getHands().size());
        System.out.println("Hands: " + currentPlayer.getHands());
        System.out.println("Owned Buildings: " + currentPlayer.getBuildings());
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
            payWages(currentPlayer);
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
        // 所持物件の価値を計算する
        for (Player player : this.players) {
            player.calcScore();
        }

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
    }

    private boolean isAllPlayersDone() {
        boolean isAllPlayersDone = true;
        // TODO stream
        for (Player player : this.players) {
            if (player.isActive())
                return false;
        }
        return isAllPlayersDone;
    }

    private void payWages(Player player) {

        // トータル賃金
        int totalWages = player.getWorkers().size() * this.currentWage;

        // 賃金を所持金でまかなえなければ所持物件を売る
        List<Card> buildings = player.getBuildings();
        int count = 0;
        while (player.getMoney() < totalWages
                && buildings.stream().filter(c -> c.getCategory() != CardCategory.FACILITY).count() > 0) {
            if (player instanceof HumanPlayer) {
                System.out.println("売却するカードを選択してください");
                Display.printChoices(player.getBuildings());
                int option = Display.scanNextInt(player.getBuildings().size());
                player.sellBuildings(this.board, option);
            } else {
                // todo stuckの可能性あり。とりあえずランダムで抜けられる
                AIPlayer ai = (AIPlayer) player;
                int option;
                if (count > 10) {
                    option = new Random().nextInt(ai.getBuildings().size());
                    System.out.println("infinite loop detected... Auto-piloting initiated");
                } else {
                    option = ai.getBrain().thinkSell(ai, this.board);
                }
                ai.sellBuildings(this.board, option);
            }
            count++;
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
            // 選択肢を表示
            System.out.println();
            System.out.println("1 => 公共フィールド");
            System.out.println(this.board.getBuildings());
            System.out.println("2 => 自フィールド");
            System.out.println(currentPlayer.getBuildings());
            System.out.println();

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
            Card card = area.get(optionB);

            // カードを使用する
            done = card.apply(currentPlayer, this.board);
            if (!done) {
                System.out.println("丸投げだけど、何らかの理由で使用できません。もう一度最初から");
            }
        } while (!done);
    }

    private void AIPlayerTurn(Player currentPlayer) {

        AIPlayer ai = (AIPlayer) currentPlayer;
        boolean done = false;
        int stuck = -1;
        do {
            stuck++;
            if (stuck >= this.maxStucks) {
                // 強制で鉱山を使わせる
                // このブロックを下に置くとcontinueの時に実行されず無限ループ
                // TODO
                System.out.println("stuck...");
                System.out.println("forced-piloting initiated");
                this.board.getBuildings().get(0).apply(ai, this.board);
                break;
            }

            // Humanplayerの時と違って、どのエリアかも一緒にリターンしてくる
            List<Integer> options = ai.getBrain().thinkUseCard(ai, this.board, stuck);
            System.out.println("AI wants to use the card at: " + options);

            int areaChoice = options.get(0);
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

            // 選択されたエリアからカードを取得
            Card card = area.get(options.get(1));
            System.out.println(card);

            // カードを使用する
            try {
                done = card.apply(ai, this.board);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            System.out.println("AttemptSuccess? " + done);
            try {
                Thread.sleep(this.waitTime);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }

        } while (!done);
    }

    // TODO
    // public void addTrash(Card c) {
    // this.board.getTrash().add(c);
    // }

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

    public int getCurrentWage() {
        return currentWage;
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

    public int getGDP() {
        return this.board.getGdp();
    }

    public boolean isRandomDeck() {
        return this.isRandomDeck;
    }

    public boolean hasManyPlayers() {
        return this.hasManyPlayers;
    }
    // #endregion

}