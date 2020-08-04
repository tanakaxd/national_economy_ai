package NE.player.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import NE.board.Board;
import NE.player.Player;

public class AhoAI implements IAI {

    @Override
    public List<Integer> thinkUseCard(Player self, Board board, int stucks) {

        return new ArrayList<>(Arrays.asList(new Random().nextInt(100), new Random().nextInt(100)));
    }

    @Override
    public int thinkBuild(Player self, Board board, Set<Integer> indexesNotAllowed) {
        return new Random().nextInt(100);
    }

    @Override
    public int thinkDiscard(Player self, Board board, Set<Integer> indexesNotAllowed) {
        return new Random().nextInt(100);
    }

    @Override
    public int thinkSell(Player self, Board board) {
        return new Random().nextInt(100);
    }

}