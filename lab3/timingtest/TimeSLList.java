package timingtest;
import java.util.Random;

import edu.princeton.cs.algs4.Stopwatch;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        timeGetLast(Ns, times, opCounts);
        printTimingTable(Ns, times, opCounts);
    }

    public static void timeGetLast(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        // TODO: YOUR CODE HERE
        int n = 1000;
        int ops = 10000;

        SLList<Integer> test;
        // Add N items to the SLList
        // start the timer
            while (n <= 128000) {
                test = new SLList<>();
                for (int j = 0; j < n; j++) {
                    test.addLast(1);
                }

                Stopwatch sw = new Stopwatch();
                for (int j = 0; j < ops; j++) {
                    test.getLast();
                }
                double time = sw.elapsedTime();

                Ns.addLast(n);
                times.addLast(time);
                opCounts.addLast(ops);
                n *= 2;
            }
        }

}
