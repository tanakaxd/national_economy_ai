package NE.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.card.Card.CardCategory;
import NE.display.Display;
import NE.player.HumanPlayer;
import NE.player.Player;
import NE.player.Worker;
import NE.player.ai.AIPlayer;
import NE.player.ai.RandomAI;
import NE.player.ai.SimpleTAI;

public class GameManager {

    private int maxTurn = 9;
    private int currentTurn = 1;
    private int cardsInDeck = 40;
    private int waitTime = 0;
    private int currentWage = 2;
    private boolean isSinglePlay;
    private Board board;
    private List<Player> players = new ArrayList<>();
    private Player parentPlayer;

    private static GameManager theInstance;

    private GameManager() {
        this.board = new Board(this.cardsInDeck);
        // this.players.add(new HumanPlayer(this.board));
        this.players.add(new AIPlayer(this.board, new SimpleTAI()));
        this.players.add(new AIPlayer(this.board, new RandomAI()));
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
                System.out.println();
                System.out.println("Name: " + currentPlayer.getName());
                System.out.println();
                System.out.println("ActionCounts: " + currentPlayer.getActionCount());
                System.out.println("OwnedWorkers: " + currentPlayer.getWorkers().size());
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
        finish();
    }

    private void initTurn() {

        System.out.println();
        System.out.println("Turn: " + this.currentTurn);
        System.out.println();

        // 賃金上昇処理
        if (this.currentTurn <= 2) {
            this.currentWage = 2;
        } else if (this.currentTurn <= 5) {
            this.currentWage = 3;
        } else if (this.currentTurn <= 7) {
            this.currentWage = 4;
        } else {
            this.currentWage = 5;
        }

        // 公共の建物を建てる TODO
        // 仮仕様でランダムなカードを公共エリアに
        // this.board.getBuildings().add(board.getDeck().draw());
        // this.board.getBuildings().add(new SchoolLesser());

        // 全てのフィールド上のカードを労働可能にする
        this.board.unbanAll();

        for (Player player : this.players) {
            player.initForTurn();
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

        this.currentTurn++;
    }

    private void capHands(Player currentPlayer) {
        List<Card> hands = currentPlayer.getHands();
        if (hands.size() <= currentPlayer.getHandLimits())
            return;

        int discardsAmount = hands.size() - currentPlayer.getHandLimits();

        if (currentPlayer instanceof HumanPlayer) {
            // HumanPlayerの場合、選択を一気にできるようPlayer#discardを利用する
            boolean isSuccess = false;
            do {
                List<Integer> indexesToDiscard = new ArrayList<>();
                System.out.println("手札上限を超える分を捨ててください");
                Display.printChoices(hands);
                for (int i = 0; i < discardsAmount; i++) {
                    indexesToDiscard.add(Display.scanNextInt(hands.size()));
                }
                isSuccess = currentPlayer.discard(board, indexesToDiscard, discardsAmount);
                if (!isSuccess) {
                    System.out.println("必要量を満たしていません。もう一度やり直してください");
                }
            } while (!isSuccess);
        } else {
            // AIの方はとりあえず捨てたいカードを一枚ずつ聞く形式にする
            AIPlayer ai = (AIPlayer) currentPlayer;
            while (hands.size() > currentPlayer.getHandLimits()) {
                int option = ai.getBrain().discard(ai, this.board);
                if (0 <= option && option < hands.size()) {
                    currentPlayer.discard(board, ai.getHands().get(option));
                } else {
                    // 不適切な値なら強制的にindex0のカードを捨てさせる
                    currentPlayer.discard(board, ai.getHands().get(0));
                }
            }
        }
    }

    private void finish() {
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
            // for (Worker worker : player.getWorkers()) {
            // if (worker.isWorkable())
            // return false;
            // }
            if (player.isActive())
                return false;
        }
        return isAllPlayersDone;
    }

    private void payWages(Player player) {

        // トータル賃金
        int totalWages = player.getWorkers().size() * this.currentWage;

        // 賃金を所持金でまかなえなければ所持物件を売る
        // 売れない物件はまだ未実装 TODO
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
            Card card = area.get(optionB);
            // カードに応じて、使用したときの選択肢の入力を求めリストとして受け取る
            List<Integer> options = card.promptChoice(currentPlayer, this.board);

            // カードを使用する
            done = card.apply(currentPlayer, this.board, options);
            if (!done) {
                System.out.println("丸投げだけど、何らかの理由で使用できません");
            }
        } while (!done);
    }

    private void AIPlayerTurn(Player currentPlayer) {

        AIPlayer player = (AIPlayer) currentPlayer;
        boolean done = false;
        int stuck = -1;
        do {
            stuck++;
            if (stuck >= 3) {
                // 強制で鉱山を使わせる
                // このブロックを下に置くとcontinueの時に実行されず無限ループ
                // TODO
                System.out.println("stuck...");
                System.out.println("forced-piloting initiated");
                List<Integer> options = new ArrayList<>();
                done = this.board.getBuildings().get(0).apply(player, this.board, options);
                break;
            }

            // Humanplyaerの時と違って、どのエリアで、どのカードを使うかも含めてreturnしてくる
            List<Integer> options = player.getBrain().think(player, this.board, stuck);
            System.out.println("AI's decision: " + options);

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

            // 選択されたエリアからカードを取得
            Card card = area.get(options.remove(0));

            System.out.println(options);
            System.out.println(card);

            // カードを使用する
            done = card.apply(player, this.board, options);

            System.out.println("AttemptSuccess? " + done);
            try {
                Thread.sleep(this.waitTime);
            } catch (InterruptedException e) {
                // e.printStackTrace();
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
        List<Card> trash = this.board.getTrash().stream().filter(c -> c.getCategory() != CardCategory.COMMODITY)
                .collect(Collectors.toList());
        while (trash.size() > 0) {
            this.board.getDeck().addCard(trash.remove(0));
        }
    }

    // #region setter/getter
    public int getCurrentWage() {
        return currentWage;
    }
    // #endregion
}