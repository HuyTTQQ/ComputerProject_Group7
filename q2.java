package mas;

import java.util.*;

public class q2 {

    //  Bài 1: Derangement 
    public static long factorial(int n) {
        long f = 1;
        for (int i = 2; i <= n; i++) f *= i;
        return f;
    }

    public static long countDerangement(int n) {
        if (n == 0) return 1;
        if (n == 1) return 0;

        long[] D = new long[n + 1];
        D[0] = 1;
        D[1] = 0;
        for (int i = 2; i <= n; i++) {
            D[i] = (i - 1) * (D[i - 1] + D[i - 2]);
        }
        return D[n];
    }

    public static double probDerangement(int n) {
        return (double) countDerangement(n) / factorial(n);
    }

    //  Bài 2: Hill-distance 
    public static int hd(int a, int b) {
        if (a < b) return 2 * (b - a);
        else if (a > b) return a - b;
        else return 0;
    }

    public static int hdTotal(int[] arr) {
        int total = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            total += hd(arr[i], arr[i + 1]);
        }
        return total;
    }

    public static void estimateHillDistance(int n, int trials) {
        Random rand = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i + 1;

        double sum = 0, sumSq = 0;
        for (int t = 0; t < trials; t++) {
            shuffleArray(arr, rand);
            int hd = hdTotal(arr);
            sum += hd;
            sumSq += hd * hd;
        }

        double mean = sum / trials;
        double variance = (sumSq / trials) - (mean * mean);

        System.out.printf("n = %2d | Hill-Distance E(X) ≈ %.3f, V(X) ≈ %.3f\n", n, mean, variance);
    }

    //  Bài 3: LIS 
    public static int lengthOfLIS(int[] arr) {
        int[] dp = new int[arr.length];
        int len = 0;
        for (int num : arr) {
            int i = Arrays.binarySearch(dp, 0, len, num);
            if (i < 0) i = -(i + 1);
            dp[i] = num;
            if (i == len) len++;
        }
        return len;
    }

    public static void estimateLIS(int n, int trials) {
        Random rand = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i + 1;

        double sum = 0;
        for (int t = 0; t < trials; t++) {
            shuffleArray(arr, rand);
            sum += lengthOfLIS(arr);
        }

        double expected = sum / trials;
        System.out.printf("n = %2d | E(LIS) ≈ %.3f (gần với %.3f)\n", n, expected, 2 * Math.sqrt(n));
    }

    // Shuffle 
    public static void shuffleArray(int[] arr, Random rand) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
        }
    }

    //  Hàm main 
    public static void main(String[] args) {
        int trials = 100000; // Số lần mô phỏng
        int[] testNs = {3, 5, 7, 10};

        System.out.println("==== Bài 1: Xác suất Derangement ====");
        for (int n : testNs) {
            double prob = probDerangement(n);
            System.out.printf("n = %2d | P(derangement) = %.6f\n", n, prob);
        }

        System.out.println("\n==== Bài 2: Hill-Distance ====");
        for (int n : testNs) {
            estimateHillDistance(n, trials);
        }

        System.out.println("\n==== Bài 3: Longest Increasing Subsequence ====");
        for (int n : testNs) {
            estimateLIS(n, trials);
        }
    }
}
