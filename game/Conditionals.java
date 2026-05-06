//conditionals.java

package game;

import models.CoolBoard;
import models.pieces.Pawn;
import models.pieces.Rook;
import models.pieces.Knight;
import models.pieces.Bishop;
import models.pieces.Queen;
import models.pieces.King;

public class Conditionals {
	String [] currentPiece;
	int first;
	int sec;
	
	public Conditionals (String [] currentPiece) {
		this.currentPiece = currentPiece;
	}
	
	public void setFirstHalf (String [] currentPiece) {
		char c = Character.toUpperCase(currentPiece[0].charAt(0));
		this.first = c - 'A';
		
	}
	
	public void setSecondHalf (String [] currentPiece) {
		this.sec = 8 - Integer.parseInt(currentPiece[1]);;
	}
	
	public int getFirstHalf() {
		return first;
	}
	
	public int getSecondHalf() {
		return sec;
	}
	
	public boolean exitGame (String input) {
		if (input.equals("EXIT")) {
			return false;
		}
		return true;
	}
	
	public boolean blankSpace(String[][] board, int row, int col) {
		return board[row][col].equals("--");
	}
	
	public boolean OutOfBounds(int one, int two) {
		return ((one >= 0 && one <= 7) && (two >= 0 && two <= 7));
	}
	
	public boolean isWhiteTurn(int round) {
		if (round % 2 == 0) {
			return true;
		} 
		return false;
	}
	
	public boolean illegalMovementCheck(String pieceName, String toWhere, String fromWhere, String [][] boardData) {
		int toCol = Character.toUpperCase(toWhere.charAt(0)) - 'A';
		int toRow = 8 - (toWhere.charAt(1) - '0');
		
		int fromCol = Character.toUpperCase(fromWhere.charAt(0)) - 'A';
		int fromRow = 8 - (fromWhere.charAt(1) - '0');
		
		switch (pieceName) {
			case "BP":
			case "WP":
				return new Pawn().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
			case "BR":
			case "WR":
				return new Rook().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
			case "BN":
			case "WN":
				return new Knight().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
			case "BB":
			case "WB":
				return new Bishop().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
			case "BQ":
			case "WQ":
				return new Queen().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
			case "BK":
			case "WK":
				return new King().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
		}
		return false;
	}
	
	public boolean isValidFormat(String s) {
		char col = Character.toLowerCase(s.charAt(0));
		char row = s.charAt(1);

		boolean validCol = (col >= 'a' && col <= 'h');
		boolean validRow = (row >= '1' && row <= '8');

		return validCol && validRow; 
	}

    public boolean isKingInCheck(String[][] boardData, boolean whiteTurn) {
        char kingColor = whiteTurn ? 'W' : 'B';
        char enemyColor = whiteTurn ? 'B' : 'W';

        int kingRow = -1, kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                String piece = boardData[r][c];
                if (!piece.equals("--") && piece.charAt(0) == kingColor && piece.charAt(1) == 'K') {
                    kingRow = r;
                    kingCol = c;
                }
            }
        }

        if (kingRow == -1) {
            return false;
        }

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                String piece = boardData[r][c];
                if (piece.equals("--")) continue;
                if (piece.charAt(0) != enemyColor) continue;

                if (canPieceAttackSquare(boardData, piece, r, c, kingRow, kingCol)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean canPieceAttackSquare(String[][] boardData,
                                         String piece, int fromRow, int fromCol,
                                         int toRow, int toCol) {

        switch (piece) {
            case "WP":
            case "BP":
                return new Pawn().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
            case "WR":
            case "BR":
                return new Rook().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
            case "WN":
            case "BN":
                return new Knight().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
            case "WB":
            case "BB":
                return new Bishop().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
            case "WQ":
            case "BQ":
                return new Queen().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
            case "WK":
            case "BK":
                return new King().pieceMovement(boardData, toRow, toCol, fromRow, fromCol);
        }
        return false;
    }

    public boolean isCheckmate(String[][] boardData, boolean whiteTurn) {
        if (!isKingInCheck(boardData, whiteTurn)) {
            return false;
        }
        return !hasAnyLegalMove(boardData, whiteTurn);
    }

    public boolean isStalemate(String[][] boardData, boolean whiteTurn) {
        if (isKingInCheck(boardData, whiteTurn)) {
            return false;
        }
        return !hasAnyLegalMove(boardData, whiteTurn);
    }

    private boolean hasAnyLegalMove(String[][] boardData, boolean whiteTurn) {
        char color = whiteTurn ? 'W' : 'B';

        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {

                String piece = boardData[fromRow][fromCol];
                if (piece.equals("--")) continue;
                if (piece.charAt(0) != color) continue;

                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {

                        if (fromRow == toRow && fromCol == toCol) continue;

                        if (!canPieceAttackSquare(boardData, piece, fromRow, fromCol, toRow, toCol)) {
                            continue;
                        }

                        // simulate move and see if king is still in check
                        String[][] copy = simulateMove(boardData, fromRow, fromCol, toRow, toCol);
                        if (!isKingInCheck(copy, whiteTurn)) {
                            return true; // found at least one legal move
                        }
                    }
                }
            }
        }

        return false;
    }

    private String[][] simulateMove(String[][] boardData,
                                    int fromRow, int fromCol,
                                    int toRow, int toCol) {

        String[][] copy = new String[8][8];
        for (int r = 0; r < 8; r++) {
            System.arraycopy(boardData[r], 0, copy[r], 0, 8);
        }

        copy[toRow][toCol] = copy[fromRow][fromCol];
        copy[fromRow][fromCol] = "--";

        return copy;
    }
}
