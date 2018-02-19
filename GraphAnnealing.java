import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import static java.lang.Math.*;

public class GraphAnnealing {

	public static ArrayList<Point> swap(ArrayList<Point> a, int i, int j) {
		Point first = a.get(i);
		Point second = a.get(j);
		a.set(i, second);
		a.set(j, first);
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

	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<Point> points = new ArrayList<>();
		Scanner in = new Scanner(new File("image.txt"));
		Random random = new Random();
		int n = in.nextInt();

		for (int i = 0; i < n; i++) {
			String line = in.next();
			for (int j = 0; j < n; j++) {
				char c = line.charAt(j);
				if (c == '#') {
					Point point = new Point(i, j);
					points.add(point);
				}
			}
		}

		in.close();
		Collections.shuffle(points);

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

			tryChange = swap(tryChange, i, j);
			
			double newEnergy = fullGraphEnergy(tryChange);
//			if (i < j) {
//				newEnergy = energy + localEnergyChange(points, i, j);
//			} else {
//				newEnergy = energy + localEnergyChange(points, j, i);
//			}
			if (newEnergy <= energy) {
				points = swap(points, i, j);
				energy = newEnergy;
			} else {
				double p = exp(-(newEnergy - energy) / temperature);
				if (p > random()) {
					points = swap(points, i, j);
					energy = newEnergy;
				}
			}

			temperature = newTemp(temperature);

			System.out.println("Generation: " + gen + " Energy " + energy);
			if (gen >= 1e7) {
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
	}
}