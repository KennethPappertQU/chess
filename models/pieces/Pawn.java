//pawn.java
package SER120.chess.models.pieces;

public class Pawn extends ChessPieces {

    public boolean pieceMovement(String[][] boardData,
                                 int toRow, int toCol,
                                 int fromRow, int fromCol) {

        String piece = boardData[fromRow][fromCol];
        char color = piece.charAt(0);

        int direction = (color == 'W') ? -1 : 1;  
        int startRow  = (color == 'W') ? 6 : 1;

        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);

        String target = boardData[toRow][toCol];

        if (colDiff == 0 && rowDiff == direction) {
            return target.equals("--");
        }

        if (colDiff == 0 && rowDiff == 2 * direction && fromRow == startRow) {
            return boardData[fromRow + direction][fromCol].equals("--")
                    && target.equals("--");
        }

        if (colDiff == 1 && rowDiff == direction) {
            return !target.equals("--") && target.charAt(0) != color;
        }

        return false;
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
