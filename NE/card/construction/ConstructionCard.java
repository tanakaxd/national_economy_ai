package NE.card.construction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public abstract class ConstructionCard extends Card {

    protected int minHands;
    protected int amountsToBuild;

    public ConstructionCard() {

    }

    // @Override
    // public boolean work(Player player, Board board, List<Integer> options) {
    // // 共通している処理は、最初と最後の処理
    // // 最初のやつは、立てられるカードがない時等のreturn false
    // // 最後のやつはコスト分のカードを捨てる処理
    // List<Card> hands = player.getHands();
    // if (hands.size() < this.minHands || this.isWorked)
    // return false;

    // if (!doWork(player, board, options))
    // return false;

    // // コストを支払う
    // List<Integer> modifiedOptions =
    // options.stream().skip(1).distinct().sorted().collect(Collectors.toList());
    // for (int j : modifiedOptions) {
    // System.out.print(j);
    // }
    // for (int i = 0; i < cost; i++) {
    // player.discard(board, options.get(i));
    // }

    // return true;
    // }

    // protected abstract boolean doWork(Player player, Board board, List<Integer>
    // options);

    // @Override
    // public List<Integer> promptChoice(Player player, Board board) {
    // // TODO Auto-generated method stub
    // List<Integer> options = new ArrayList<>();
    // System.out.println("建設するカードを選んでください");
    // int option1 = Display.scanNextInt(player.getHands().size());
    // options.add(option1);
    // // System.out.println("コスト分のカードを捨てる必要があります");
    // int cost = player.getHands().get(option1).getCost();
    // System.out.println("捨てるカードを" + cost + "枚選んでください");
    // for (int i = 0; i < cost; i++) {
    // options.add(Display.scanNextInt(player.getHands().size()));
    // }

    // return options;
    // }

}