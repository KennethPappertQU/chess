//rook.java
package models.pieces;

public class Rook extends ChessPieces {

    public boolean pieceMovement(String[][] boardData,
                                 int toRow, int toCol,
                                 int fromRow, int fromCol) {

        String piece = boardData[fromRow][fromCol];
        char color = piece.charAt(0);

        // Rook must move in straight lines
        boolean straight = (fromRow == toRow || fromCol == toCol);
        if (!straight) {
            return false;
        }

        // Path must be clear
        if (!isPathClear(boardData, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        String target = boardData[toRow][toCol];

        // Cannot capture own piece
        if (target.charAt(0) == color) {
            return false;
        }

        return true;
    }

    private static boolean isPathClear(String[][] boardData,
                                       int fromRow, int fromCol,
                                       int toRow, int toCol) {

        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);

        int r = fromRow + rowStep;
        int c = fromCol + colStep;

        while (r != toRow || c != toCol) {
            if (!boardData[r][c].equals("--")) {
                return false;
            }
            r += rowStep;
            c += colStep;
        }

        return true;
    }
	public boolean isPieceAlive(String piece, String[][] boardData) {
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length; j++) {
                if (boardData[i][j].equals(piece)) {
                    return true;
                }
            }
        }
        return false;
    }
}

