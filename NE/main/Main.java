package NE.main;

import java.io.IOException;

import NE.player.ai.tai.TAIGeneExtractor;

public class Main {

    // setting GameManager
    public static final boolean IS_TAI_RECORDING = false;

    public static void main(String[] args) throws IOException {
        if (IS_TAI_RECORDING)
            TAIGeneExtractor.getInstance().init();

        GameManager.getInstance().init();
        GameManager.getInstance().run();
    }
}
