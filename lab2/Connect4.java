package lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameSearchConfigurator;
import sac.game.GameState;
import sac.game.MinMax;
import sac.game.GameStateImpl;

public class Connect4 extends GameStateImpl{

	public static final boolean IS_O_AI = true;
	public static final boolean IS_X_AI = false;
	
	public static final int m = 6;
	public static final int n = 7;	
	
	public static final byte O = 1; // gracz MAX
	public static final byte EMPTY = 0;
	public static final byte X = -1; // gracz MIN
	public static final String[] SYMBOLS = new String[] {"X", ".", "O"};
	public static boolean regulaSufitu = true;
	public byte[][] board;
	public int lastMoveI = -1;
	public int lastMoveJ = -1;
	
	public Connect4() {
		board = new byte[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				board[i][j] = EMPTY;
	}
	
	public Connect4(Connect4 parent) {
		board = new byte[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				board[i][j] = parent.board[i][j];
		setMaximizingTurnNow(parent.isMaximizingTurnNow()); //ustawia ktory gracz wykonuje ruch min czy max
	}	
	
	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		for (int j = 0; j < n; j++) {
			txt.append(" ");
			txt.append(Integer.toString(j));
		}
		txt.append("\n");
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				txt.append("|");
				txt.append(SYMBOLS[board[i][j] + 1]);
			}
			txt.append("|\n");
		}
		for (int j = 0; j < n; j++) {
			txt.append(" ");
			txt.append(Integer.toString(j));
		}
		return txt.toString();
	}
	
	public boolean move(int j) {
		int i = m - 1;
		for (; i >= 0; i--)
			if (board[i][j] == EMPTY)
				break;
		if (i < 0)
			return false;
		board[i][j] = (isMaximizingTurnNow()) ? O : X;
		setMaximizingTurnNow(!isMaximizingTurnNow());
		lastMoveI = i;
		lastMoveJ = j;
		return true;
	}
	public boolean isFilled() {
        for (int j = 0; j < n; j++) {
            if (board[0][j] != EMPTY) { //jesli ostatnie nie jest puste to wygrane
                return true;
        }
        }
	    return false;
	}
	public boolean isWin() {
		if ((lastMoveI == -1) || (lastMoveJ == -1))
			return false;
		byte symbol = board[lastMoveI][lastMoveJ];
		
		 if (regulaSufitu && isFilled()) {
		        return true;
		    }

	// prawo-lewo
		int count = 0;
		for (int k = 1; k < 4; k++) {
			if ((lastMoveJ + k >= n) || (board[lastMoveI][lastMoveJ + k] != symbol))
				break;
			count++;
		}
		for (int k = 1; k < 4; k++) {
			if ((lastMoveJ - k < 0) || (board[lastMoveI][lastMoveJ - k] != symbol))
				break;
			count++;
		}
		if (count >= 3)
			return true;
		
		// dol-gora
		count = 0;
		for (int k = 1; k < 4; k++) {
			if ((lastMoveI + k >= m) || (board[lastMoveI + k][lastMoveJ] != symbol))
				break;
			count++;
		}
		for (int k = 1; k < 4; k++) {
			if ((lastMoveI - k < 0) || (board[lastMoveI - k][lastMoveJ] != symbol))
				break;
			count++;
		}
		if (count >= 3)
			return true;

		// SW-NE
		count = 0;
		for (int k = 1; k < 4; k++) {
			if ((lastMoveI + k >= m) || (lastMoveJ - k < 0) || (board[lastMoveI + k][lastMoveJ - k] != symbol))
				break;
			count++;
		}
		for (int k = 1; k < 4; k++) {
			if ((lastMoveI - k < 0) || (lastMoveJ + k >= n) || (board[lastMoveI - k][lastMoveJ + k] != symbol))
				break;
			count++;
		}
		if (count >= 3)
			return true;
		
		// SE-NW
		count = 0;
		for (int k = 1; k < 4; k++) {
			if ((lastMoveI + k >= m) || (lastMoveJ + k >= n) || (board[lastMoveI + k][lastMoveJ + k] != symbol))
				break;
			count++;
		}
		for (int k = 1; k < 4; k++) {
			if ((lastMoveI - k < 0) || (lastMoveJ - k < 0) || (board[lastMoveI - k][lastMoveJ - k] != symbol))
				break;
			count++;
		}
		if (count >= 3)
			return true;
		
		
		return false;
	}
	
	public boolean isTie() {
		for (int j = 0; j < n; j++)
			if (board[0][j] == EMPTY)
				return false;
		return true;
	}
		
	@Override
	public List<GameState> generateChildren() {
		List<GameState> children = new ArrayList<>();
		for (int j = 0; j < n; j++) {
			Connect4 child = new Connect4(this);
			if (child.move(j)) {
				children.add(child);
				child.setMoveName(Integer.toString(j));
			}
		}
		return children;
	}
	
	@Override
	public int hashCode() {
		byte[] flat = new byte[m * n];
		int k = 0;
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				flat[k++] = board[i][j];
		return Arrays.hashCode(flat);
	}
	
	public static void main(String[] args) {
		Connect4 c4 = new Connect4();
		Connect4.setHFunction(new Connect4Evaluation());
		System.out.println(c4);
		Scanner scanner = new Scanner(System.in);
		GameSearchAlgorithm algo = new AlphaBetaPruning();//new MinMax();
		GameSearchConfigurator conf = new GameSearchConfigurator();
		conf.setDepthLimit(5.5);
		algo.setConfigurator(conf);
		while (true) {
			if (IS_O_AI) {
				algo.setInitial(c4);
				algo.execute();
				int bestMove = Integer.valueOf(algo.getFirstBestMove());
				c4.move(bestMove);
				System.out.println("MOVES SCORES: " + algo.getMovesScores()); 
				System.out.println("STATES: " + algo.getClosedStatesCount());
				System.out.println("DEPTH REACHED: " + algo.getDepthReached());
				System.out.println("TIME [ms]: " + algo.getDurationTime());
				System.out.println("MOVE PLAYED: " + bestMove);
			}
			else {
				boolean moveLegal = false;
				do {
					System.out.print("O PLAYER YOUR MOVE: ");
					int j = scanner.nextInt();
					moveLegal = c4.move(j);
				} while (!moveLegal);
			}
			System.out.println(c4);
			if (c4.isWin()) {
				System.out.println("O PLAYER WINS!");
				break;
			}
			if (c4.isTie()) {
				System.out.println("GAME TIED.");
				break;
			}
			
			if (IS_X_AI) {
				algo.setInitial(c4);
				algo.execute();
				int bestMove = Integer.valueOf(algo.getFirstBestMove());
				c4.move(bestMove);
				System.out.println("MOVES SCORES: " + algo.getMovesScores()); 
				System.out.println("STATES: " + algo.getClosedStatesCount());
				System.out.println("DEPTH REACHED: " + algo.getDepthReached());
				System.out.println("TIME [ms]: " + algo.getDurationTime());
				System.out.println("MOVE PLAYED: " + bestMove);
			}
			else {
				boolean moveLegal = false;
				do {
					System.out.print("X PLAYER YOUR MOVE: ");
					int j = scanner.nextInt();
					moveLegal = c4.move(j);
				} while (!moveLegal);
			}
			System.out.println(c4);
			if (c4.isWin()) {
				System.out.println("X PLAYER WINS!");
				break;
			}
			if (c4.isTie()) {
				System.out.println("GAME TIED.");
				break;
			}			
		}
		scanner.close();
	}
}