package NE.display;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
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
        if (amounts <= 0) {
            System.out.println("invalid scan");
            return -1;
        }

        int option = 0;
        boolean ok = false;
        do {
            try {
                option = new Scanner(System.in).nextInt();
                if (option >= 1 && option <= amounts) {

                    ok = true;
                } else {
                    System.out.println("***無効な入力です。もう一回やってみて***");
                }
            } catch (InputMismatchException e) {
                System.out.println("半角で入力してください");
            }
        } while (!ok);
        return option - 1;

    }

    public static void myLog(String s) {
        // String className = this.getClass().getName();
        System.out.print(Thread.currentThread().getStackTrace()[2].getLineNumber());
        System.out.println(s);
    }

    public static int myRandom(int maxExclusive) {
        if (maxExclusive == 0) {
            return 0;
        }
        return new Random().nextInt(maxExclusive);
    }

    public static double RandomGaussian(double average, double sigma) {

        double X, Y;
        double Z1;

        X = Math.random();
        Y = Math.random();

        Z1 = sigma * Math.sqrt(-2.0 * Math.log(X)) * Math.cos(2.0 * Math.PI * Y) + average;
        // Z2 = Math.Sqrt(-2.0 * Math.Log(X)) * Math.Sin(2.0 * Math.PI * Y);

        return Z1;
    }
}