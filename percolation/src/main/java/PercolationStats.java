import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double C = 1.96;

    private final double mean;
    private final double stdDev;
    private final double lo;
    private final double hi;

    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        if (trials <= 0) {
            throw new IllegalArgumentException();
        }
        double[] results = new double[trials];
        Percolation p;
        for (int i = 0; i < trials; i++) {
            p = new Percolation(n);
            do {
                p.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            } while (!p.percolates());
            results[i] = 1. * p.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(results);
        stdDev = StdStats.stddev(results);
        lo = mean - C * stddev() / Math.sqrt(trials);
        hi = mean + C * stddev() / Math.sqrt(trials);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stdDev;
    }

    public double confidenceLo() {
        return lo;
    }

    public double confidenceHi() {
        return hi;
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.printf("%-24s= %.16f\n", "mean", ps.mean());
        System.out.printf("%-24s= %.16f\n", "stddev", ps.stddev());
        System.out.printf("%-24s= [%.16f, %.16f]\n", "95% confidence interval", ps.confidenceLo(), ps.confidenceHi());

    }

}