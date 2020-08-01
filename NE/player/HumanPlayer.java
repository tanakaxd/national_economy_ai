package NE.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;

public class HumanPlayer extends Player {

    private static int id = 1;

    public HumanPlayer(Board board) {
        super(board);
        this.name = "Player-" + id;
        id++;
    }

    @Override
    public List<Integer> askDiscard(Board board, int cost, List<Integer> indexesNotAllowed) {
        return this.askDiscard(board, cost);
    }

    @Override
    public List<Integer> askDiscard(Board board, int cost) {
        System.out.println("捨てるカードを" + cost + "枚選んでください");
        Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < cost) {
            indexes.add(Display.scanNextInt(this.hands.size()));
        }
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> askBuild(Board board, int amounts, Card card) {
        System.out.println("建設するカードを選んでください");
        Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < amounts) {
            indexes.add(Display.scanNextInt(this.hands.size()));
        }
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }
}