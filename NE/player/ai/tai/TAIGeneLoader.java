package NE.player.ai.tai;

// import static NE.card.Card.CardCategory.CONSTRUCTION;
// import static NE.card.Card.CardCategory.EDUCATION;
// import static NE.card.Card.CardCategory.FACILITY;
// import static NE.card.Card.CardCategory.INDUSTRY;
// import static NE.card.Card.CardCategory.MARKET;
import static NE.card.Card.CardCategory.AGRICULTURE;
import static NE.card.Card.CardCategory.CONSTRUCTION;
import static NE.card.Card.CardCategory.EDUCATION;
import static NE.card.Card.CardCategory.FACILITY;
import static NE.card.Card.CardCategory.INDUSTRY;
import static NE.card.Card.CardCategory.MARKET;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import NE.card.Card.CardCategory;
import NE.display.Display;

public class TAIGeneLoader {

    // private Map<CardCategory, Integer> personalityGene;
    private List<Map<CardCategory, Integer>> currentGenerationPool = new ArrayList<>();
    private Map<CardCategory, Integer> specificGene = new LinkedHashMap<>();
    private int[] specificData = new int[] { 136, 70, 90, 91, 167, 10 };
    private boolean hasData = false;

    private static TAIGeneLoader theInstance;

    private TAIGeneLoader() {

        this.hasData = loadGenePool();
        if (specificData.length == 6) {
            specificGene.put(AGRICULTURE, specificData[0]);
            specificGene.put(CONSTRUCTION, specificData[1]);
            specificGene.put(INDUSTRY, specificData[2]);
            specificGene.put(MARKET, specificData[3]);
            specificGene.put(EDUCATION, specificData[4]);
            specificGene.put(FACILITY, specificData[5]);
        }

    }

    public static TAIGeneLoader getInstance() {
        if (theInstance == null) {
            theInstance = new TAIGeneLoader();
        }
        return theInstance;
    }

    private boolean loadGenePool() {
        int[][] retrievedData;
        try {
            retrievedData = Files
                    // 読み込むCSVファイルを設定
                    .lines(Paths.get("NE/player/ai/tai/TAINewGenerationPool.csv"))
                    // ファイルから一行ずつ読み込んで、「,」で区切った文字列配列へ変換
                    .map(line -> line.split(","))
                    // 文字列配列をint配列に変換
                    .map(array -> {
                        int[] b = new int[array.length];
                        for (int i = 0; i < b.length; i++)
                            b[i] = Integer.valueOf(array[i]);
                        return b;
                    })
                    // TODO リストとかにできるか
                    // 二次元配列で出力
                    .toArray(int[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (retrievedData.length == 0) {
            // genePoolファイルが存在しない場合、もしくは存在するがデータが不十分な場合
            return false;
        }

        for (int[] indivisualData : retrievedData) {
            Map<CardCategory, Integer> indivisual = new LinkedHashMap<>();
            indivisual.put(AGRICULTURE, indivisualData[0]);
            indivisual.put(CONSTRUCTION, indivisualData[1]);
            indivisual.put(INDUSTRY, indivisualData[2]);
            indivisual.put(MARKET, indivisualData[3]);
            indivisual.put(EDUCATION, indivisualData[4]);
            indivisual.put(FACILITY, indivisualData[5]);

            this.currentGenerationPool.add(indivisual);
        }

        return true;
    }

    public Map<CardCategory, Integer> getPersonalityData(GeneMode geneMode) {
        switch (geneMode) {
            case SPECIFIC:
                return this.specificGene;
            case GENETIC_ALGORITHM:
                if (this.hasData) {
                    // TODO ランダムは均質的にならない場合がある。できたら、最初から一人ずつ取り出したい
                    return this.currentGenerationPool.get(new Random().nextInt(this.currentGenerationPool.size()));
                }
                break;
            case RANDOM:
                return new LinkedHashMap<CardCategory, Integer>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    {
                        put(AGRICULTURE, (int) Display.RandomGaussian(100, 50));
                        put(CONSTRUCTION, (int) Display.RandomGaussian(100, 50));
                        put(INDUSTRY, (int) Display.RandomGaussian(100, 50));
                        put(MARKET, (int) Display.RandomGaussian(100, 50));
                        put(EDUCATION, (int) Display.RandomGaussian(100, 50));
                        put(FACILITY, (int) Display.RandomGaussian(100, 50));
                    }
                };
            default:
                System.out.println("invalid GeneMode");
                break;
        }
        return null;
    }

    public enum GeneMode {
        RANDOM, GENETIC_ALGORITHM, SPECIFIC
    }

}