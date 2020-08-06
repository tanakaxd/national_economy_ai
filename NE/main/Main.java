package NE.main;

import java.io.IOException;

import NE.player.ai.tai.TAIGeneExtractor;

public class Main {

    // setting GameManager
    public static final boolean IS_TAI_RECORDING = false;

    public static void main(String[] args) throws IOException {
        if (IS_TAI_RECORDING)
            TAIGeneExtractor.getInstance().init();

        // ここでGMをinstantiateして、ボード、デッキ、プレイヤーの生成をする
        GameManager.getInstance().init();
        // ここでボードの最初の公共職場を建てる
        GameManager.getInstance().initBoardBuildings();
        // ループ開始
        GameManager.getInstance().run();
    }
}
