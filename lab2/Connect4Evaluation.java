package lab2;

import sac.State;
import sac.StateFunction;

public class Connect4Evaluation extends StateFunction{
	@Override 
	public double calculate(State state) {
		Connect4 c4 = (Connect4) state;
		
		if (c4.isWin()) {
			return (c4.isMaximizingTurnNow()) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		}
		else {
			if(!Connect4.regulaSufitu)
			return evaluateBoard(c4);
			else
				return evaluateBoardSufit(c4);
		}
	    }

	 private double evaluateBoard(Connect4 c4) {
	        double score = 0.0;
	        int centerColumn = Connect4.n / 2;

	        //priorytet dla centrum
	        for (int i = 0; i < Connect4.m; i++) {
	            if (c4.board[i][centerColumn] == Connect4.O) {
	                score += 10; 
	            } else if (c4.board[i][centerColumn] == Connect4.X) {
	                score -= 10;
	            }
	        }

	        
	        score += evaluateHorizontal(c4);
	        score += evaluateVertical(c4);
	        score += evaluateDiagonal(c4);

	        return score;
	    }

	    private double evaluateHorizontal(Connect4 c4) {
	        double score = 0.0;
	        for (int i = 0; i < Connect4.m; i++) {
	            for (int j = 0; j <= Connect4.n - 4; j++) {
	                score += evaluateFrame(c4, i, j, 0, 1);
	            }
	        }
	        return score;
	    }

	    private double evaluateVertical(Connect4 c4) {
	        double score = 0.0;
	        for (int i = 0; i <= Connect4.m - 4; i++) {
	            for (int j = 0; j < Connect4.n; j++) {
	                score += evaluateFrame(c4, i, j, 1, 0);
	            }
	        }
	        return score;
	    }

	    private double evaluateDiagonal(Connect4 c4) {
	        double score = 0.0;
	        //diag od lewego gornego do prawego dolnego
	        for (int i = 0; i <= Connect4.m - 4; i++) {
	            for (int j = 0; j <= Connect4.n - 4; j++) {
	                score += evaluateFrame(c4, i, j, 1, 1);
	            }
	        }
	        //diag od lewego dolnego do prawego gornego
	        for (int i = 3; i < Connect4.m; i++) {
	            for (int j = 0; j <= Connect4.n - 4; j++) {
	                score += evaluateFrame(c4, i, j, -1, 1);
	            }
	        }
	        return score;
	    }
	    
	    private double evaluateFrame(Connect4 c4, int startI, int startJ, int deltaI, int deltaJ) {
	        int countO = 0;
	        int countX = 0;

	        for (int k = 0; k < 4; k++) {
	            int i = startI + k * deltaI;
	            int j = startJ + k * deltaJ;

	            if (c4.board[i][j] == Connect4.O) {
	                countO++;
	            } else if (c4.board[i][j] == Connect4.X) {
	                countX++;
	            }
	        }
	        //jak w ramie istnieje tez element przeciwnego gracza to ne jest ta rama brana pod uwage
	        if (countO > 0 && countX > 0) {
	            return 0;
	        }

	        //ocena na podstawie liczby pionkow w ramie
	        if (countO > 0) {
	            switch (countO) {
	                case 1:
	                    return 1; 
	                case 2:
	                    return 5; 
	                case 3:
	                    return 50;  
	            }
	        } else if (countX > 0) {
	            switch (countX) {
	                case 1:
	                    return -1; 
	                case 2:
	                    return -5;  
	                case 3:
	                    return -50; 
	            }
	        }

	        return 0;
	    }
	    public boolean isFilled(Connect4 c4) {
		    for (int i = Connect4.m-1; i >= 2; i--) { //sprawdz do przedostatniego wiersza
		        for (int j = Connect4.n-1; j >= 0; j--) {
		            if (c4.board[i][j] == Connect4.EMPTY) { //jesli jest puste pole to nie
		                return false;
		            }
		        }
		    }
		    return true;
		}
	    
	    private double evaluateBoardSufit(Connect4 c4) {
	        double score = 0.0;
	        int centerColumn = Connect4.n / 2;
	        for (int j = Connect4.n -1; j >= 0; j--) {
	        if(!isFilled(c4) && c4.board[1][j]==Connect4.EMPTY) {
	        	 if (c4.board[1][j] == Connect4.O) {
		                return Double.NEGATIVE_INFINITY;
		            } else if (c4.board[1][j] == Connect4.X) {
		                return Double.POSITIVE_INFINITY;
		            }
	        }
	        }
	        
	        for (int i = 0; i < Connect4.m; i++) {
	            if (c4.board[i][centerColumn] == Connect4.O) {
	                score += 10; 
	            } else if (c4.board[i][centerColumn] == Connect4.X) {
	                score -= 10;
	            }
	        }

	        
	        score += evaluateHorizontalSufit(c4);
	        score += evaluateVerticalSufit(c4);
	        score += evaluateDiagonalSufit(c4);

	        return score;
	    }
	    private double evaluateHorizontalSufit(Connect4 c4) {
	        double score = 0.0;
	        for (int i = 1; i < Connect4.m; i++) {
	            for (int j = 0; j <= Connect4.n - 4; j++) {
	                score += evaluateFrame(c4, i, j, 0, 1);
	            }
	        }
	        return score;
	    }

	    private double evaluateVerticalSufit(Connect4 c4) {
	        double score = 0.0;
	        for (int i = 1; i <= Connect4.m - 4; i++) {
	            for (int j = 0; j < Connect4.n; j++) {
	                score += evaluateFrame(c4, i, j, 1, 0);
	            }
	        }
	        return score;
	    }

	    private double evaluateDiagonalSufit(Connect4 c4) {
	        double score = 0.0;
	        //diag od lewego gornego do prawego dolnego
	        for (int i = 1; i <= Connect4.m - 4; i++) {
	            for (int j = 0; j <= Connect4.n - 4; j++) {
	                score += evaluateFrame(c4, i, j, 1, 1);
	            }
	        }
	        //diag od lewego dolnego do prawego gornego
	        for (int i = 4; i < Connect4.m; i++) {
	            for (int j = 0; j <= Connect4.n - 4; j++) {
	                score += evaluateFrame(c4, i, j, -1, 1);
	            }
	        }
	        return score;
	    }
}


