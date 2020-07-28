package NE.display;

import java.util.List;
import java.util.Scanner;

public class Display {
    public static <T> void printChoices(List<T> l) {
        for (int i = 0; i < l.size(); i++) {
            System.out.println(i + 1 + ": " + l.get(i).toString());
        }
    }

    public static <T> T printChoicesAndScan(List<T> l) {
        for (int i = 0; i < l.size(); i++) {
            System.out.println(i + 1 + ": " + l.get(i).toString());
        }

        int option = scanNextInt(l.size());

        return l.get(option);
    }

    public static int scanNextInt(int amounts) {
        int option;
        boolean ok = false;
        do {
            option = new Scanner(System.in).nextInt();
            if (option >= 1 && option <= amounts) {

                ok = true;
            } else {
                System.out.println("***無効な入力です。もう一回やってみて***");
            }
        } while (!ok);

        return option - 1;
    }
}