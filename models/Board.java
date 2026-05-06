//Board.java

package SER120.chess.models;

public class Board {

    protected int numRows;
    protected int numCols;

    protected String[][] boardData;

    public Board(int rows, int cols) {
        this.numRows = rows;
        this.numCols = cols;
        this.boardData = new String[rows][cols];
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                boardData[i][j] = "--";
            }
        }
    }

    public void initializeBoardPieces() {
        boardData[0][0] = "BR";
        boardData[0][1] = "BN";
        boardData[0][2] = "BB";
        boardData[0][3] = "BQ";
        boardData[0][4] = "BK";
        boardData[0][5] = "BB";
        boardData[0][6] = "BN";
        boardData[0][7] = "BR";

        for (int j = 0; j < 8; j++) {
            boardData[1][j] = "BP";
        }

        boardData[7][0] = "WR";
        boardData[7][1] = "WN";
        boardData[7][2] = "WB";
        boardData[7][3] = "WQ";
        boardData[7][4] = "WK";
        boardData[7][5] = "WB";
        boardData[7][6] = "WN";
        boardData[7][7] = "WR";

        for (int j = 0; j < 8; j++) {
            boardData[6][j] = "WP";
        }
    }

    public void showBoard() {
        System.out.println("\n--- Current Chess Board ---");

        for (int row = 0; row < numRows; row++) {
            printHorizontalDivider();
            System.out.print("| ");

            for (int col = 0; col < numCols; col++) {
                System.out.print(boardData[row][col] + " | ");
            }

            System.out.println();
        }

        printHorizontalDivider();
    }

    private void printHorizontalDivider() {
        for (int k = 0; k < numCols; k++) {
            System.out.print("-----");
        }
        System.out.println("-");
    }

    public boolean isSquare() {
        return numCols == numRows;
    }

    public String getPieceAt(int row, int col) {
        return boardData[row][col];
    }

    public void setPieceAt(int row, int col, String piece) {
        boardData[row][col] = piece;
    }

    public String[][] getBoardData() {
        return boardData;
    }
}
