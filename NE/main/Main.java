package NE.main;

import java.io.IOException;

import NE.player.ai.tai.TAIGeneExtractor;

public class Main {
    public static void main(String[] args) throws IOException {
        TAIGeneExtractor.getInstance().init();

        GameManager.getInstance().init();
        GameManager.getInstance().run();
    }
}
