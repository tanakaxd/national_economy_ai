package NE.player.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class RandomAI implements IAI {

    public RandomAI() {

    }

    @Override
    public List<Integer> thinkUseCard(Player self, Board board, int stucks) {

        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        int fieldChoice = r.nextInt(2);// どのフィールドを使うか
        list.add(fieldChoice);
        List<Card> field = fieldChoice == 0 ? board.getBuildings() : self.getBuildings();

        list.add(Display.myRandom(field.size()));// どのカードを使うか

        return list;
    }

    @Override
    public int thinkBuild(Player self, Board board, Set<Integer> indexesNotAllowed) {
        return Display.myRandom(self.getHands().size());
    }

    @Override
    public int thinkDiscard(Player self, Board board, Set<Integer> indexesNotAllowed) {
        return Display.myRandom(self.getHands().size());
    }

    @Override
    public int thinkSell(Player self, Board board) {
        return Display.myRandom(self.getBuildings().size());
    }

}