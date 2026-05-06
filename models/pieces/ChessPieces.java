//ChessPieces.java
package SER120.chess.models.pieces;

public abstract class ChessPieces {
	private String firstChar;
	private String secChar;

    public abstract boolean pieceMovement(String[][] boardData,
                                          int toRow, int toCol,
                                          int fromRow, int fromCol);
	
	public abstract boolean isPieceAlive(String piece, String [][] boardData);
	
	private void setFirstChar(String piece) {
		this.firstChar = piece.charAt(0) + "";
	}
	
	private void setSecChar(String piece) {
		this.secChar = piece.charAt(1) + "";
	}
	
	private String getFirstChar() {
		return firstChar;
	}
	
	private String getSecChar () {
		return secChar;
	}
}
