package NE.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import NE.player.ai.tai.TAI;

public class CsvImporter {

    private static CsvImporter theInstance;
    private Map<Integer, Integer> cardData;

    private CsvImporter() {

        loadCardData();

    }

    public static void main(String[] args) throws java.io.IOException {
        // int[][] ret = Files
        // // 読み込むCSVファイルを設定
        // .lines(Paths.get("NE\\data\\NationalEconomyCardData.csv"))
        // // ファイルから一行ずつ読み込んで、「,」で区切った文字列配列へ変換
        // .map(line -> line.split(",")).skip(1)
        // // 文字列配列を変換
        // .map(array -> {
        // int[] b = new int[2];
        // b[0] = Integer.valueOf(array[0]);
        // b[1] = Integer.valueOf(array[12]);
        // return b;
        // })
        // // 二次元配列で出力
        // .toArray(int[][]::new);

        // // 得られたboolean二次元配列の中身をコンソールに出力
        // for (int[] array : ret) {
        // System.out.println(Arrays.toString(array));
        // }

        // String s = "true";
        // boolean bool = Boolean.valueOf(s);
        // if (bool) {
        // System.out.println("OK");
        // }
        // System.out.println(bool);
        // System.out.println(s);
    }

    public static CsvImporter getInstance() {
        if (theInstance == null) {
            theInstance = new CsvImporter();
        }
        return theInstance;
    }

    public Map<Integer, Integer> getCardData() {
        return this.cardData;
    }

    public void loadCardData() {
        int[][] ret;
        try {
            ret = Files
                    // 読み込むCSVファイルを設定
                    .lines(Paths.get("NE\\data\\NationalEconomyCardData.csv"))
                    // ファイルから一行ずつ読み込んで、「,」で区切った文字列配列へ変換
                    .map(line -> line.split(",")).skip(1)
                    // 文字列配列を変換
                    .map(array -> {
                        int[] b = new int[2];
                        b[0] = Integer.valueOf(array[0]);
                        b[1] = Integer.valueOf(array[12]);
                        return b;
                    })
                    // 二次元配列で出力
                    .toArray(int[][]::new);

            // 得られたboolean二次元配列の中身をコンソールに出力
            // for (int[] array : ret) {
            // System.out.println(Arrays.toString(array));
            // }

            Map<Integer, Integer> result = new HashMap<>();

            // Mapに変換
            for (int[] is : ret) {
                result.put(is[0], is[1]);
            }

            // System.out.println(result);
            this.cardData = result;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}