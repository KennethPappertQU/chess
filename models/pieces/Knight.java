//knight.java
package SER120.chess.models.pieces;

public class Knight extends ChessPieces {

    public boolean pieceMovement(String[][] boardData,
                                        int toRow, int toCol,
                                        int fromRow, int fromCol) {

        String piece = boardData[fromRow][fromCol];
        char color = piece.charAt(0);

        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        boolean isKnightMove =
                (rowDiff == 2 && colDiff == 1) ||
                (rowDiff == 1 && colDiff == 2);

        if (!isKnightMove) {
            return false;
        }

        String target = boardData[toRow][toCol];

        // Cannot capture own piece
        if (target.charAt(0) == color) {
            return false;
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

