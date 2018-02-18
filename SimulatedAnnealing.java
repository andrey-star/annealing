import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.*;

public class SimulatedAnnealing {

	public static ArrayList<Integer> swap(ArrayList<Integer> a, int i, int j) {
		int first = a.get(i);
		int second = a.get(j);
		a.set(i, second);
		a.set(j, first);
		return a;
	}

	public static double fullEnergy(ArrayList<Integer> a) {
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
		for(int i = 0; i < n; i++) {
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

	public static void main(String[] args) {
		ArrayList<Integer> pos = new ArrayList<>();
		int n = 200;
		Random random = new Random();

		for (int i = 0; i < n; i++) {
			pos.add(i);
		}

		Collections.shuffle(pos);

		double energy = fullEnergy(pos);
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
			double newEnergy = fullEnergy(tryChange);
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
		show(pos);
		System.out.println("\nTime passed: " + (endTime - startTime)*1.0/1000 + "s");
	}

}
