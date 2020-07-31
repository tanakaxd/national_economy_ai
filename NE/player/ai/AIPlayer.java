package NE.player.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class AIPlayer extends Player {

    private IAI brain;

    private static int id = 1;

    public AIPlayer(Board board, IAI brain) {
        super(board);
        this.brain = brain;
        this.name = "AI-" + id + ":brain=" + this.brain.getClass().getName();
        id++;
    }

    public IAI getBrain() {
        return brain;
    }

    public void setBrain(IAI brain) {
        this.brain = brain;
    }

    @Override
    public List<Integer> askDiscard(Board board, int cost) {

        System.out.println("捨てるカードを" + cost + "枚選んでください");
        Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < cost) {
            indexes.add(this.brain.thinkDiscard(this, board, indexes));
        }
        System.out.println("AI chose: " + indexes);
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> askBuild(Board board, Card card) {

        // TODO multiple
        System.out.println("建設するカードを選んでください");
        Display.printChoices(this.hands);
        Set<Integer> indexes = new HashSet<>();
        indexes.add(this.brain.thinkBuild(this, board, indexes));
        System.out.println("AI chose: " + indexes);
        return new ArrayList<>(indexes).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

}