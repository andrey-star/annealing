import static java.lang.Math.*;

import java.util.Random;

public class GeneticAlgorithm {

	public static void view(int[][] a) {
		int n = a.length;
		for(int i = 0; i < n; i++) {
			System.out.print(" _");
		}
		System.out.println();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print("|");
				if (a[i][j] == 1) {
					System.out.print("x");
				} else {
					System.out.print("_");
				}
			}
			System.out.println("|");
		}
	}
	
	
	static class Entity {
		int[][] position;
		int size;
		int energy;
		int a;
		int b;
		
		public Entity(int n, int a, int b) {
			this.size = n;
			this.a = a;
			this.b = b;
			position = new int[n][n];
		}
		
		public void addPiece(int x, int y) {
			this.position[x][y] = 1;
		}
		
		public void updateEnergy() {
			this.energy = a*this.countAllHits() - b*this.countPieces();
		}
		
		public void randomFill() {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (random() > 0.5) {
						this.addPiece(i, j);
					}
				}
			}
			updateEnergy();
		}
		
		
		
		public int countPieces() {
			int pieces = 0;
			
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (this.position[i][j] == 1) {
						pieces++;
					}
				}
			}
			
			return pieces;
		}
		
		public void view() {
			for(int i = 0; i < size; i++) {
				System.out.print(" _");
			}
			System.out.println();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					System.out.print("|");
					if (this.position[i][j] == 1) {
						System.out.print("x");
					} else {
						System.out.print("_");
					}
				}
				System.out.println("|");
			}
		}
		
		public int countHits(int x, int y) {
			if (position[x][y] == 0) {
				return 0;
			}
			int hits = 0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if ((i != x || j != y) && ((abs(x - i) + abs(y - j) <= 2 && abs(x - i) <= 1 && abs(y - j) <= 1) || (abs(x - i) + abs(y - j) == 3 && abs(x - i) <= 2 && abs(y - j) <= 2))) {
						if (position[i][j] == 1) {
							hits++;
						}
					}
				}
			}
			return hits;
		}
		
		public int countAllHits() {
			int hits = 0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					hits += countHits(i, j);
				}
			}
			return hits;
		}
		
		public void change() {
			Random random = new Random();
			int x = random.nextInt(this.size);
			int y = random.nextInt(this.size);
//			int[][] tryChange = new int[size][size];
//			
//			for (int i = 0; i < size; i++) {
//				for (int j = 0; j < size; j++) {
//					tryChange[i][j] = this.position[i][j];
//				}
//			}
			
			int oldEnergy = this.energy;
			
			if (this.position[x][y] == 0) {
				this.position[x][y] = 1;
			} else {
				this.position[x][y] = 0;
			}
			
			this.updateEnergy();
			
			if (oldEnergy <= energy) {
				energy = oldEnergy;
				if (this.position[x][y] == 0) {
					this.position[x][y] = 1;
				} else {
					this.position[x][y] = 0;  
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		int n = 9;
		int a = 3;
		int b = 6;
		Entity board = new Entity(n, a, b);
		board.randomFill();
		board.view();
		System.out.println(board.energy);
		int gen = 0;
		int oldEnergy = board.energy;
		while (gen++ < 1000000) {
			board.change();
			if (board.energy < oldEnergy) {
				System.out.println(board.energy);
				oldEnergy = board.energy;
			}
		}
		
		board.view();
		System.out.println(board.energy);
		System.out.println(board.countAllHits());
	}

}
