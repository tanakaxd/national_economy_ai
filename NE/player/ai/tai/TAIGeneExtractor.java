package NE.player.ai.tai;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import NE.card.Card.CardCategory;
import NE.player.AIPlayer;
import NE.player.Player;

public class TAIGeneExtractor {
    // TAIのpersonalityデータをゲームが始まる前に抽出して、ゲームが終わった時書き出す
    // 5試合、計20人分のデータになるまで付け加えていき、20人になったら別ファイルに書いていく
    // 読み込むときに20人のデータが集まっていたら、MergeOperatorに報せて次世代を産ませる
    // つまり、このクラスはデータを読み込んで状況を確認することと、データを書き込むことを両方行う

    // 一つのデータが持つべき情報。generation, index, ranking, score, fitness, personality
    // e.g. [1,3,1,78,125,80,90,60,120,100,120]

    private static TAIGeneExtractor theInstance;
    private final String PATH = "NE/player/ai/tai/TAIPersonalityScoreData.csv";

    private int currentGeneration = 1;// ファイルを読み込んだとき、末尾の個体のgenerationを取得する
    private int lastIndex;// ファイルを読み込んだとき、末尾の個体のindexを取得する。世代が変わらなければ、書き込むときそこから順次増やしていく.
    private int nextIndex = 1;// 1~
    private int nextGeneration = 1;// curretnGenerationの個体数が一定数溜まっていたら+1する。ファイルを書き込むときに使う
    private int numbersPerGen = 40;
    private boolean needNextGen = false;// 現在の世代が一定数を超えていたら、trueになる
    private int[][] retrievedData;

    private TAIGeneExtractor() {

    }

    public static TAIGeneExtractor getInstance() {
        if (theInstance == null) {
            theInstance = new TAIGeneExtractor();
        }
        return theInstance;
    }

    // ゲームが始まる前
    public void init() {
        // ファイルを読み込む
        try {
            this.retrievedData = Files
                    // 読み込むCSVファイルを設定
                    .lines(Paths.get(PATH))
                    // ファイルから一行ずつ読み込んで、「,」で区切った文字列配列へ変換
                    .map(line -> line.split(","))
                    // 文字列配列をint配列に変換
                    .map(array -> {
                        int[] b = new int[array.length];
                        for (int i = 0; i < b.length; i++)
                            b[i] = Integer.valueOf(array[i]);
                        return b;
                    })
                    // 二次元配列で出力
                    .toArray(int[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 成績データが一定数溜まっているかチェック
        // MergeOperatorにその情報を送りinvoke
        // trueの場合、データも一緒に送ってしまう？
        if (this.retrievedData == null || this.retrievedData.length == 0) {
            return;
            // TAIMergeOperator.getInstance().invoke(false, Collections.emptyMap());
        } else {
            // データを一部抽出して保持。ゲーム終了後、データを書きだすのに使う
            this.currentGeneration = this.retrievedData[this.retrievedData.length - 1][0];
            this.lastIndex = this.retrievedData[this.retrievedData.length - 1][1];
            this.needNextGen = this.lastIndex >= this.numbersPerGen;
            this.nextIndex = this.needNextGen ? 1 : this.lastIndex + 1;
            this.nextGeneration = this.needNextGen ? this.currentGeneration + 1 : this.currentGeneration;

            if (this.needNextGen) {
                // 世代交代が必要
                // データを送信用に加工したい
                Map<Map<CardCategory, Integer>, Integer> processedData = new LinkedHashMap<>();
                // TODO processedDataをmapにすると、keyになるmapが同一でなくとも同値である限り同じ個体とみなされてしまう
                // 結果として重複とみなされ排除されてしまう
                // [[[CONSTRUCTION,100],[MARKET,80],[FACILITY,90]],fitness]
                // [[[CONSTRUCTION,80],[MARKET,120],[FACILITY,90]],fitness]

                // numbersPerGen人分のデータをとりたい
                for (int i = this.retrievedData.length - 1; processedData.size() < this.numbersPerGen && i >= 0; i--) {

                    int[] singleData = new int[6];
                    for (int j = 0; j < singleData.length; j++) {
                        int offset = 5;
                        singleData[j] = this.retrievedData[i][j + offset];
                    }

                    int fitness = this.retrievedData[i][4];
                    // #region このやり方でもできるが、引数で与えられた6つのマップがlinkedHashMapにならない。順序付けができなくなる
                    // AGRICULTURE, CONSTRUCTION, INDUSTRY, MARKET, EDUCATION, FACILITY, COMMODITY,
                    // processedData.put(Map.ofEntries(
                    // new AbstractMap.SimpleEntry<CardCategory, Integer>(CardCategory.AGRICULTURE,
                    // singleData[0]),
                    // new AbstractMap.SimpleEntry<CardCategory, Integer>(CardCategory.CONSTRUCTION,
                    // singleData[1]),
                    // new AbstractMap.SimpleEntry<CardCategory, Integer>(CardCategory.INDUSTRY,
                    // singleData[2]),
                    // new AbstractMap.SimpleEntry<CardCategory, Integer>(CardCategory.MARKET,
                    // singleData[3]),
                    // new AbstractMap.SimpleEntry<CardCategory, Integer>(CardCategory.EDUCATION,
                    // singleData[4]),
                    // new AbstractMap.SimpleEntry<CardCategory, Integer>(CardCategory.FACILITY,
                    // singleData[5])),
                    // fitness);
                    // #endregion

                    Map<CardCategory, Integer> person = new LinkedHashMap<>();
                    person.put(CardCategory.AGRICULTURE, singleData[0]);
                    person.put(CardCategory.CONSTRUCTION, singleData[1]);
                    person.put(CardCategory.INDUSTRY, singleData[2]);
                    person.put(CardCategory.MARKET, singleData[3]);
                    person.put(CardCategory.EDUCATION, singleData[4]);
                    person.put(CardCategory.FACILITY, singleData[5]);
                    processedData.put(person, fitness);

                }

                TAIMergeOperator.getInstance().invoke(processedData);
            } else {
                return;
                // TAIMergeOperator.getInstance().invoke(false, Collections.emptyMap());
            }
        }

    }

    // ゲームが終わった時
    public void terminate(List<Player> rankingData) {
        // もともとあるデータに新しいデータを足していく
        List<int[]> updatedData = new ArrayList<>(Arrays.asList(this.retrievedData));

        // TAIのみ抽出
        List<AIPlayer> filteredRanking = rankingData.stream().filter(p -> p instanceof AIPlayer).map(p -> (AIPlayer) p)
                .filter(p -> p.getBrain() instanceof TAI).collect(Collectors.toList());

        if (filteredRanking.isEmpty())
            return;

        for (int i = 0; i < filteredRanking.size(); i++) {
            AIPlayer player = filteredRanking.get(i);
            int rank = i + 1;
            int fitness = calcFitness(rank, player.getScore());
            List<Integer> firstPart = new ArrayList<>(
                    Arrays.asList(this.nextGeneration, this.nextIndex + i, rank, player.getScore(), fitness));
            List<Integer> secondPart = new ArrayList<>();
            for (Integer integer : ((TAI) player.getBrain()).getPERSONALITY_FAVOR().values()) {
                secondPart.add(integer);
            }

            firstPart.addAll(secondPart);

            int[] array = firstPart.stream().mapToInt(num -> num).toArray();

            updatedData.add(array);
        }

        // 一つのデータが持つべき情報。generation, index, ranking, score, fitness, personality
        // e.g. [1,3,1,78,125, 80,90,60,120,100,120]

        BufferedWriter wr = null;
        try {
            wr = new BufferedWriter(new FileWriter(new File(PATH)));
            StringBuilder sb = new StringBuilder();// TODO stringbuilderのbufferサイズ？
            for (int[] row : updatedData) {
                for (int is : row) {
                    sb.append(is).append(",");
                }
                sb.append("\n");
            }

            wr.write(sb.toString());
            wr.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int calcFitness(int ranking, int score) {
        // TODO いかようにも変更しうる
        return (int) (score >= 0 ? Math.pow((double) score / 10, 2) : 0);
    }

}