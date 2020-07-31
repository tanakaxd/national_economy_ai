package NE.player.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import NE.board.Board;
import NE.card.Card;
import NE.display.Display;
import NE.player.Player;

public class RandomAI implements IAI {

    public RandomAI() {

    }

    @Override
    public List<Integer> think(Player self, Board board, int stucked) {

        // List<Integer> list = new ArrayList<>(Arrays.asList(1, 4, 1, 3));
        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        int fieldChoice = r.nextInt(2);// どのフィールドを使うか
        list.add(fieldChoice);
        List<Card> field = fieldChoice == 0 ? board.getBuildings() : self.getBuildings();

        list.add(Display.myRandom(field.size()));// どのカードを使うか

        for (int i = 0; i < 10; i++) {
            list.add(Display.myRandom(self.getHands().size()));
        }

        return list;
    }

    @Override
    public int discard(Player self, Board board) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int sell(Player self, Board board) {
        // TODO Auto-generated method stub
        return 0;
    }

}