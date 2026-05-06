//Coolboard.java

package SER120.chess.models;

public class CoolBoard extends Board {

    // ANSI Escape Codes for Colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String BLACK_BG = "\u001B[40m";
    public static final String WHITE_TEXT = "\u001B[37m";
    public static final String YELLOW_TEXT = "\u001B[33m";

    public CoolBoard(int rows, int cols) {
        super(rows, cols); // Call the original Board constructor
        boardData = new String [8][8];
        initializeBoard();
    }

    public void showBoard() {
		System.out.println(YELLOW_TEXT + "\n=== â™› CHESS ADVENTURE BOARD â™› ===" + RESET);
        
        for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				String background = ((i + j) % 2 == 0) ? GREEN_BG : BLACK_BG;
				String piece = boardData[i][j];

				System.out.print(background + " " + piece + " " + RESET);
			}
			System.out.println();
		}
        System.out.println(YELLOW_TEXT + "================================" + RESET);
    }

	public void initializeBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boardData[i][j] = "--";
			}
		}

		boardData[0] = new String[]{"BR","BN","BB","BQ","BK","BB","BN","BR"};
		boardData[1] = new String[]{"BP","BP","BP","BP","BP","BP","BP","BP"};
	  
		boardData[6] = new String[]{"WP","WP","WP","WP","WP","WP","WP","WP"};
		boardData[7] = new String[]{"WR","WN","WB","WQ","WK","WB","WN","WR"};
	}

	public String[][] getBoardData() {
		String[][] copy = new String[8][8];
		for (int i = 0; i < 8; i++) {
			System.arraycopy(boardData[i], 0, copy[i], 0, 8);
		}
		return copy;
	}

	public void movePiece(String from, String to) {
		int fromCol = Character.toUpperCase(from.charAt(0)) - 'A';
		int fromRow = 8 - (from.charAt(1) - '0');

		int toCol = Character.toUpperCase(to.charAt(0)) - 'A';
		int toRow = 8 - (to.charAt(1) - '0');

		String piece = boardData[fromRow][fromCol];

		if (piece.equals("--")) {
			System.out.println("Replay error: no piece at " + from);
			return;
		}

		boardData[toRow][toCol] = piece;
		boardData[fromRow][fromCol] = "--";
	}
}

