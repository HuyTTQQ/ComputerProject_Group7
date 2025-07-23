package q1_projectmas_group7;

import java.util.*;

public class Q1 {
    static Random rand = new Random();

    public static List<Character> Toss(double p, int n) {
        List<Character> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(rand.nextDouble() < p ? 'H' : 'T');
        }
        return result;
    }

    public static double relativeFrequencyOfH(double p, int N) {
        List<Character> tosses = Toss(p, N);
        long countH = tosses.stream().filter(ch -> ch == 'H').count();
        return (double) countH / N;
    }

    public static int countRuns(List<Character> tosses) {
        if (tosses.isEmpty()) return 0;
        int runs = 1;
        for (int i = 1; i < tosses.size(); i++) {
            if (!tosses.get(i).equals(tosses.get(i - 1))) {
                runs++;
            }
        }
        return runs;
    }

    public static void simulateRuns(double p, int n, int N) {
        double sum = 0, sumSq = 0;
        for (int i = 0; i < N; i++) {
            List<Character> tosses = Toss(p, n);
            int runs = countRuns(tosses);
            sum += runs;
            sumSq += runs * runs;
        }
        double mean = sum / N;
        double variance = sumSq / N - mean * mean;

        System.out.printf("Mean of Runs ≈ %.5f\n", mean);
        System.out.printf("Variance of Runs ≈ %.5f\n", variance);
        System.out.printf("Expected Mean ≈ %.5f\n", 1 + 2 * (n - 1) * p * (1 - p));
        System.out.printf("Expected Variance ≈ %.5f\n",
                2 * p * (1 - p) * (2 * n - 3 - 2 * p * (1 - p) * (3 * n - 5)));
    }

    public static int tossUntilHeadExceedsTail(double p) {
        int heads = 0, tails = 0, tosses = 0;
        while (heads <= tails) {
            tosses++;
            if (rand.nextDouble() < p) heads++;
            else tails++;
        }
        return tosses;
    }

    public static void simulateEX(double p, int N) {
        double sum = 0;
        for (int i = 0; i < N; i++) {
            sum += tossUntilHeadExceedsTail(p);
        }
        System.out.printf("Estimated E(X) ≈ %.5f\n", sum / N);
    }

    public static long countCombinations(int a, int b) {
        return combination(a + b, a);
    }

    public static long combination(int n, int k) {
        long res = 1;
        for (int i = 1; i <= k; i++) {
            res *= (n - i + 1);
            res /= i;
        }
        return res;
    }

    public static String randomABString(int a, int b) {
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < a; i++) chars.add('A');
        for (int i = 0; i < b; i++) chars.add('B');
        Collections.shuffle(chars);
        StringBuilder sb = new StringBuilder();
        for (char c : chars) sb.append(c);
        return sb.toString();
    }

    public static int countRunA(String s) {
        int count = 0;
        boolean inA = false;

        for (char c : s.toCharArray()) {
            if (c == 'A') {
                if (!inA) count++;
                inA = true;
            } else {
                inA = false;
            }
        }
        return count;
    }

    public static void simulateRunA(int a, int b, int N) {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            String s = randomABString(a, b);
            sum += countRunA(s);
        }
        double mean = (double) sum / N;
        double expected = (double) a * (b + 1) / (a + b);
        System.out.printf("Simulated Mean of RunA ≈ %.5f\n", mean);
        System.out.printf("Expected Mean of RunA = %.5f\n", expected);
    }

    public static void main(String[] args) {
        double p = 0.6;
        int n = 100;
        int N = 10000;

        System.out.println("Câu 1.2");
        System.out.printf("Relative frequency of H ≈ %.5f\n", relativeFrequencyOfH(p, N));

        System.out.println("\nCâu 1.3");
        simulateRuns(p, n, 1000);

        System.out.println("\nCâu 1.4");
        simulateEX(p, 10000);

        System.out.println("\nCâu 1.5");
        int a = 10, b = 10;
        System.out.printf("Size of S(%d,%d) = %d\n", a, b, countCombinations(a, b));
        simulateRunA(a, b, 10000);
    }
}
