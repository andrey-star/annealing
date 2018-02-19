import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.*;

public class GraphAnnealing {

	public static ArrayList<Integer> swap(ArrayList<Integer> a, int i, int j) {
		int first = a.get(i);
		int second = a.get(j);
		a.set(i, second);
		a.set(j, first);
		return a;
	}

	public static double fullQueenEnergy(ArrayList<Integer> a) {
		int energy = 0;
		for (int i = 0; i < a.size(); i++) {
			for (int j = 0; j < a.size(); j++) {
				if (i != j) {
					if (abs(a.get(i) - a.get(j)) == abs(i - j)) {
						energy++;
					}
				}
			}
		}
		return energy;
	}

	public static double fullGraphEnergy(ArrayList<Point> a) {
		double energy = 0;
		for (int i = 0; i < a.size() - 1; i++) {
			System.out.println(a.get(i).x + " " + a.get(i + 1).x + " " + a.get(i).y + " " + a.get(i + 1).y);
			energy += Math.hypot(a.get(i).x - a.get(i + 1).x, a.get(i).y - a.get(i + 1).y);
		}
		return energy;
	}

	public static double indexEnergy(ArrayList<Integer> a, int i) {
		int energy = 0;
		for (int j = 0; j < a.size(); j++) {
			if (i != j) {
				if (abs(a.get(i) - a.get(j)) == abs(i - j)) {
					energy++;
				}
			}
		}
		return energy;
	}

	public static void show(ArrayList<Integer> a) {
		int n = a.size();
		for (int i = 0; i < n; i++) {
			System.out.print(" _");
		}
		System.out.println();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print("|");
				if (a.get(j) == i) {
					System.out.print("x");
				} else {
					System.out.print("_");
				}
			}
			System.out.println("|");
		}
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
	}

	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<Point> points = new ArrayList<>();
		Scanner in = new Scanner(new File("image.txt"));
		Random random = new Random();
		int n = 5;

		for (int i = 0; i < n; i++) {
			String line = in.next();
			for (int j = 0; j < n; j++) {
				char c = line.charAt(j);
				if (c == '#') {
					Point point = new Point(j, n - i - 1);
					points.add(point);
				}
			}
		}

		for (Point i : points) {
			System.out.println(i.x + " " + i.y);
		}

		Collections.shuffle(points);
		in.close();

		double energy = fullGraphEnergy(points);
		System.out.println(energy);
		System.exit(0);
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

			// tryChange = swap(tryChange, i, j);
			double newEnergy = fullGraphEnergy(tryChange);
			if (newEnergy <= energy) {
				// points = swap(points, i, j);
				energy = newEnergy;
			} else {
				double p = exp(-(newEnergy - energy) / temperature);
				if (p > random()) {
					// points = swap(points, i, j);
					energy = newEnergy;
				}
			}

			temperature = newTemp(temperature);

			System.out.println("Generation: " + gen);
			if (energy == 0) {
				break;
			}

		}

		long endTime = System.currentTimeMillis();
		// show(pos);
		System.out.println("\nTime passed: " + (endTime - startTime) * 1.0 / 1000 + "s");
	}

	public static void main2(String[] args) {
		ArrayList<Integer> pos = new ArrayList<>();
		int n = 200;
		Random random = new Random();

		for (int i = 0; i < n; i++) {
			pos.add(i);
		}

		Collections.shuffle(pos);

		double energy = fullQueenEnergy(pos);
		double temperature = 30;
		long startTime = System.currentTimeMillis();
		int gen = 0;
		while (true) {
			gen++;
			ArrayList<Integer> tryChange = new ArrayList<>(pos);
			int i = random.nextInt(pos.size());
			int j = random.nextInt(pos.size());

			while (j == i) {
				j = random.nextInt(pos.size());
			}

			tryChange = swap(tryChange, i, j);
			double newEnergy = fullQueenEnergy(tryChange);
			if (newEnergy <= energy) {
				pos = swap(pos, i, j);
				energy = newEnergy;
			} else {
				double p = exp(-(newEnergy - energy) / temperature);
				if (p > random()) {
					pos = swap(pos, i, j);
					energy = newEnergy;
				}
			}

			temperature = newTemp(temperature);

			System.out.println("Generation: " + gen);
			if (energy == 0) {
				break;
			}

		}

		long endTime = System.currentTimeMillis();
		// show(pos);
		System.out.println("\nTime passed: " + (endTime - startTime) * 1.0 / 1000 + "s");
	}

}