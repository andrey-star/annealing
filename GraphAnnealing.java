import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JApplet;

import static java.lang.Math.*;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class GraphAnnealing extends JApplet {

	
	public static ArrayList<Point> swap(ArrayList<Point> a, int i, int j) {
		Point first = a.get(i);
		Point second = a.get(j);
		a.set(i, second);
		a.set(j, first);
		return a;
	}

	public static ArrayList<Point> flip(ArrayList<Point> a, int i, int j) {

		int to = max(i, j);
		int from = min(i, j);

		for (int b = from; b < (to + from + 1) / 2; b++) {
			Point d = a.get(b);
			a.set(b, a.get(to + from - b));
			a.set(to + from - b, d);
		}

		return a;
	}

	public static double fullGraphEnergy(ArrayList<Point> a) {
		double energy = 0;
		for (int i = 0; i < a.size() - 1; i++) {
			energy += hypot(a.get(i).x - a.get(i + 1).x, a.get(i).y - a.get(i + 1).y);
		}
		return energy;
	}

	public static double localEnergyChange(ArrayList<Point> a, int i, int j) {

		if (i > 0 && j > 0 && i < a.size() - 1 && j < a.size() - 1) {
			double ai = hypot(a.get(i).x - a.get(i - 1).x, a.get(i).y - a.get(i - 1).y);
			double jd = hypot(a.get(j).x - a.get(j + 1).x, a.get(j).y - a.get(j + 1).y);
			double aj = hypot(a.get(i - 1).x - a.get(j).x, a.get(i - 1).y - a.get(j).y);
			double id = hypot(a.get(i).x - a.get(j + 1).x, a.get(i).y - a.get(j + 1).y);
			return id + aj - ai - jd;
		}

		return 0;
	}

	public static double newTemp(double temperature) {
		return temperature * 0.95;
	}

	static class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return this.x + " " + this.y;
		}
	}
	
	@Override
	public void init() {
		this.setLayout(null);
		this.setSize(2*margin + 52*K, 2*margin + 52*K);
		try {
			main(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		repaint();
	}
	
	static int margin = 10;
	
	@Override
	public void paint(Graphics g) {
		try {
			PrintWriter out = new PrintWriter(new File("output.txt"));
			for (Point point : points) {
				out.println(point.x + " " + point.y);
			}
			out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < points.size() - 1; i++) {
			g.drawLine(margin + K*points.get(i).x, margin + K*points.get(i).y, margin + K*points.get(i + 1).x, margin + K*points.get(i + 1).y);
			
		}
		for (int i = 0; i < points.size(); i++) {
			g.setColor(Color.MAGENTA);
			
			if (i == 0 || i == points.size() - 1) {
				g.setColor(Color.GREEN);
			}
			
			g.fillOval(margin + K*points.get(i).x - 5, margin + K*points.get(i).y - 5, 10, 10);
			g.setColor(Color.BLACK);
		}
	}

	static int K = 17;
	
	static ArrayList<Point> points = new ArrayList<>();
	
	public static void main(String[] args) throws FileNotFoundException {
		Random random = new Random();

		Scanner in = new Scanner(new File("output.txt"));
		int n = 52;
		
		for (int i = 0; i < 526; i++) {
			Point point = new Point(in.nextInt(), in.nextInt());
			points.add(point);
		}
		in.close();
		
		double energy = fullGraphEnergy(points);
		double temperature = 30;
		long startTime = System.currentTimeMillis();
		int gen = 0;
		while (true) {
			gen++;
			ArrayList<Point> tryChange = new ArrayList<>(points);
			int i = random.nextInt(points.size());
			int j = random.nextInt(points.size());

			while (j == i) {
				j = random.nextInt(points.size());
			}

			tryChange = flip(tryChange, i, j);

			double newEnergy = fullGraphEnergy(tryChange);

			if (newEnergy <= energy) {
				points = flip(points, i, j);
				energy = newEnergy;
			} else {
				double p = exp(-(newEnergy - energy) / temperature);
				if (p > random()) {
					points = flip(points, i, j);
					energy = newEnergy;
				}
			}

			temperature = newTemp(temperature);

			System.out.println("Generation: " + gen + " Energy " + energy);
			if (gen >= 1e6) {
				for (Point point : points) {
					String toChess = "";
					if (point.y < 26) {
						toChess += (char) (point.y + 'a');
					} else {
						toChess += (char) (point.y + 'A' - 26);
					}
					toChess += n - point.x;
					System.out.print(toChess + " ");
				}
				break;
			}

		}
		long endTime = System.currentTimeMillis();
		System.out.println("\nTime passed: " + (endTime - startTime) * 1.0 / 1000 + "s");
		System.out.println(points.toString());
	}
}