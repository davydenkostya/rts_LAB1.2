package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.FileWriter;

public class Calculations extends JPanel {

    public static void main(String[] args) throws Exception {
        Calculations points = new Calculations();
        JFrame frame = new JFrame("x(t)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(points);
        frame.setSize(1000, 1024);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Calculations m = new Calculations();
        m.freq = m.generateFrequency(m.omega, m.n);
        long start = System.currentTimeMillis();
        m.x = m.generateRandSignal(m.N, m.n, m.a, m.fi, m.freq);
        long finish = System.currentTimeMillis();
        long timeXt = finish - start;
        System.out.println("x(t) time: " + timeXt);

        double mathExpectationY = findMathExpectation(m.y, m.N);
        double mathExpectation = findMathExpectation(m.x, m.N);
        System.out.println("Math expectation: Mx = " + mathExpectation);

        double dispersion = findDispersion(m.x, mathExpectation, m.N);
        System.out.println("Dispersion: Dx = " + dispersion);

        long start_Rxx = System.currentTimeMillis();
        m.Rxx = findRxx(m.N, m.x, mathExpectation);
        long finish_Rxx = System.currentTimeMillis();
        long timeRxx = finish_Rxx - start_Rxx;
        System.out.println("time Rxx: " + timeRxx);

        long start_Rxy = System.currentTimeMillis();
        m.Rxy = findRxy(m.N, m.y, mathExpectation, mathExpectationY);
        long finish_Rxy = System.currentTimeMillis();
        long timeRxy = finish_Rxy - start_Rxy;
        System.out.println("time Rxy: " + timeRxy);

        m.writeInFile(timeXt, mathExpectation, dispersion, timeRxx, timeRxy);

    }

    int n = 12;
    int N = 1024;
    int omega = 2400;
    int[] a = generateArr(n);
    int[] aY = generateArr(n);
    int[] fi = generateArr(n);
    int[] fiY = generateArr(n);
    int[] freq = new int[n];
    double[] x = new double[N];
    double[] y = new double[N];
    double[] Rxx = new double[(N/2)-1];
    double[] Rxy = new double[(N/2)-1];

    public static double[] findRxx(int N, double[] x, double Mx) {
        double[] Rxx = new double[(N / 2) - 1];
        for (int teta = 0; teta < (N / 2) - 1; teta++) {
            for (int t = 0; t < (N / 2) - 1; t++) {
                Rxx[teta] += ((x[t] - Mx) * (x[t+teta] - Mx)) / (N - 1);
            }
        }
        return Rxx;
    }

    public static double[] findRxy(int N, double[] y, double Mx, double My) {
        double[] Rxy = new double[(N / 2) - 1];
        for (int teta = 0; teta < (N / 2) - 1; teta++) {
            for (int t = 0; t < (N / 2) - 1; t++) {
                Rxy[teta] += ((y[t] - Mx) * (y[t+teta] - My)) / (N - 1);
            }
        }
        return Rxy;
    }

    public void writeInFile(long timeXt, double mathExpectation, double dispersion, long timeRxx, long timeRxy) throws Exception {
        FileWriter nFile = new FileWriter("C:\\Users\\Dell\\Desktop\\RTS_reports\\file1.txt");
        nFile.write("x(t) time: " + timeXt + " ms\nMath expectation: Mx = " + mathExpectation + "\nDispersion: Dx = " + dispersion + "\ntime Rxx: " + timeRxx + " ms\ntime Rxy: " + timeRxy + " ms");
        nFile.close();
    }

    public int[] generateFrequency(int omega, int n) {
        int[] freq = new int[n];
        int step = omega / n;
        for (int i = 0; i < n; i++) {
            freq[i] = omega - i * step;
        }
        return freq;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D g3 = (Graphics2D) g;
        Graphics2D g4 = (Graphics2D) g;

        freq = generateFrequency(omega, n);
        x = generateRandSignal(N, n, a, fi, freq);
    	g.setColor(new Color(255, 87, 51));
		for (int i = 0; i < N; i++) {
			if (i != N - 1) {
				g2.draw(new Line2D.Double(i*10, 500 + 6 * x[i], (i + 1)*10, 500 + 6 * x[i + 1]));
			}
		}
    }

    public static int[] generateArr(int n) {
        int[] newArr = new int[n];
        for (int i = 0; i < n; i++) {
            newArr[i] = (int) (Math.random()*10) - 5;
        }
        return newArr;
    }

    public static double[] generateRandSignal(int N, int n, int[] a, int[] fi, int[] freq) {
        double[] x = new double[N];
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < n; i++) {
                x[j] += a[i] * Math.sin(freq[i] * j + fi[i]);
            }
        }
        return x;
    }

    public static double findMathExpectation(double[] x, int N) {
        double sumX = 0;
        double mx = 0;
        for (int j = 0; j < N; j++) {
            sumX += x[j];
        }
        mx = sumX / N;
        return mx;
    }

    public static double findDispersion(double[] x, double mx, int N) {
        double dx = 0;
        for (int j = 0; j < N; j++) {
            dx += (x[j] - mx) * (x[j] - mx) / (N - 1);
        }
        return dx;
    }
}
