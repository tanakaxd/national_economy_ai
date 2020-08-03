package NE.player.ai.tai;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import NE.card.Card.CardCategory;
import NE.display.Display;

public class TAIMergeOperator {
    // GeneExtractorからinvokeされる
    public final String PATH = "NE/player/ai/tai/TAINewGenerationPool.csv";
    private double mutationRate = 0.05;
    private int generationSize = 40;

    private static TAIMergeOperator theInstance;

    private TAIMergeOperator() {

    }

    public static TAIMergeOperator getInstance() {
        if (theInstance == null) {
            theInstance = new TAIMergeOperator();
        }
        return theInstance;
    }

    public void invoke(Boolean needNewPool, Map<Map<CardCategory, Integer>, Integer> data) {
        // [[[CONSTRUCTION,100],[MARKET,80],[FACILITY,90]],fitness]
        // [[[CONSTRUCTION,80],[MARKET,120],[FACILITY,90]],fitness]
        if (needNewPool) {
            // 20人分のpersonalityデータをfitnessで加重して親プールを作り、子供個体を20人生み出す
            List<Map<CardCategory, Integer>> parentPool = new ArrayList<>();
            data.forEach((k, v) -> {
                for (int i = 0; i < v; i++) {
                    parentPool.add(k);
                }
            });

            System.out.println(parentPool.size());

            List<Map<CardCategory, Integer>> childPool = new ArrayList<>();
            while (childPool.size() < this.generationSize) {
                Map<CardCategory, Integer> parentA = parentPool.get(new Random().nextInt(parentPool.size()));
                System.out.println("parentA: " + parentA);
                Map<CardCategory, Integer> parentB = parentPool.get(new Random().nextInt(parentPool.size()));
                System.out.println("parentB: " + parentB);
                Map<CardCategory, Integer> child = merge(parentA, parentB);
                System.out.println("child: " + child);
                childPool.add(child);
            }
            // Display.scanNextInt(10);

            // プールファイルを更新
            update(childPool);
            // Display.scanNextInt(10);
        } else {
            // プールは更新せず、
        }

        // GeneLoaderに報せる。mainが順次処理してくれるので今のところいらない処理 TODO
    }

    public Map<CardCategory, Integer> merge(Map<CardCategory, Integer> parentA, Map<CardCategory, Integer> parentB) {
        // AGRICULTURE, CONSTRUCTION, INDUSTRY, MARKET, EDUCATION, FACILITY, COMMODITY,
        Map<CardCategory, Integer> child = new LinkedHashMap<>();
        child.put(CardCategory.AGRICULTURE, Math.random() < this.mutationRate ? (int) Display.RandomGaussian(100, 50)
                : Math.random() > 0.5 ? parentA.get(CardCategory.AGRICULTURE) : parentB.get(CardCategory.AGRICULTURE));
        child.put(CardCategory.CONSTRUCTION,
                Math.random() < this.mutationRate ? (int) Display.RandomGaussian(100, 50)
                        : Math.random() > 0.5 ? parentA.get(CardCategory.CONSTRUCTION)
                                : parentB.get(CardCategory.CONSTRUCTION));
        child.put(CardCategory.INDUSTRY, Math.random() < this.mutationRate ? (int) Display.RandomGaussian(100, 50)
                : Math.random() > 0.5 ? parentA.get(CardCategory.INDUSTRY) : parentB.get(CardCategory.INDUSTRY));
        child.put(CardCategory.MARKET, Math.random() < this.mutationRate ? (int) Display.RandomGaussian(100, 50)
                : Math.random() > 0.5 ? parentA.get(CardCategory.MARKET) : parentB.get(CardCategory.MARKET));
        child.put(CardCategory.EDUCATION, Math.random() < this.mutationRate ? (int) Display.RandomGaussian(100, 50)
                : Math.random() > 0.5 ? parentA.get(CardCategory.EDUCATION) : parentB.get(CardCategory.EDUCATION));
        child.put(CardCategory.FACILITY, Math.random() < this.mutationRate ? (int) Display.RandomGaussian(100, 50)
                : Math.random() > 0.5 ? parentA.get(CardCategory.FACILITY) : parentB.get(CardCategory.FACILITY));

        return child;

    }

    private void update(List<Map<CardCategory, Integer>> childPool) {
        BufferedWriter wr = null;
        try {
            wr = new BufferedWriter(new FileWriter(new File(PATH)));

            StringBuilder sb = new StringBuilder(220);// TODO stringbuilderのbufferサイズ？
            for (Map<CardCategory, Integer> mapEntry : childPool) {
                for (Integer integer : mapEntry.values()) {
                    sb.append(integer).append(",");
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
}