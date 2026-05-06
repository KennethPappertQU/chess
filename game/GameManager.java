//GameManager.java
package game;

import java.io.File;
import java.util.Scanner;

import models.CoolBoard;
import game.Conditionals;
import saveload.SaveGame;
import saveload.LoadGame;

public class GameManager {

    private CoolBoard board;
    private GameReplay replay;
    private SaveGame saver;
    private LoadGame loader;

    public GameManager() {
        this.board = new CoolBoard(8, 8);
        this.replay = new GameReplay();
        this.replay.pastMovements = new String[200][2];
        this.saver = new SaveGame();
        this.loader = new LoadGame();
    }

    public void runGame() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        int round = 0;

        System.out.println("GameManager: Initializing engine...");

        while (running) {

            board.showBoard();

            System.out.println("Commands: SAVE | LOAD | REPLAY | EXIT");
            System.out.println(round % 2 == 0 ? "White to move." : "Black to move.");

            System.out.print("Enter piece location (e.g., E2): ");
            String from = scanner.nextLine().toUpperCase();

            // Handle commands
            if (from.equals("SAVE")) {
                saver.saveGameFile(new File("save.txt"), board, replay);
                continue;
            }
            if (from.equals("LOAD")) {
                loader.loadGameFile(new File("save.txt"), board, replay);
                continue;
            }
            if (from.equals("REPLAY")) {
                try {
                    replay.replaySaves(replay.pastMovements);
                } catch (Exception e) {
                    System.out.println("Replay error: " + e.getMessage());
                }
                continue;
            }
            if (from.equals("EXIT")) {
                running = false;
                break;
            }

            System.out.print("Enter destination (e.g., E4): ");
            String to = scanner.nextLine().toUpperCase();

            if (to.equals("EXIT")) {
                running = false;
                break;
            }

            Conditionals cond = new Conditionals(new String[]{});

            if (!cond.isValidFormat(from) || !cond.isValidFormat(to)) {
                System.out.println("Invalid coordinate format.");
                continue;
            }

            int fromCol = from.charAt(0) - 'A';
            int fromRow = 8 - (from.charAt(1) - '0');

            int toCol = to.charAt(0) - 'A';
            int toRow = 8 - (to.charAt(1) - '0');

            String piece = board.getPieceAt(fromRow, fromCol);

            if (piece.equals("--")) {
                System.out.println("That square is empty.");
                continue;
            }

            boolean whiteTurn = (round % 2 == 0);
            boolean isWhitePiece = piece.charAt(0) == 'W';

            if (whiteTurn && !isWhitePiece) {
                System.out.println("It is White's turn.");
                continue;
            }
            if (!whiteTurn && isWhitePiece) {
                System.out.println("It is Black's turn.");
                continue;
            }

            if (!cond.illegalMovementCheck(piece, to, from, board.getBoardData())) {
                System.out.println("Illegal move.");
                continue;
            }

            // Move piece
            board.setPieceAt(toRow, toCol, piece);
            board.setPieceAt(fromRow, fromCol, "--");

            replay.saveMovement(replay.pastMovements, from, to);

            round++;

			boolean nextTurnIsWhite = (round % 2 == 0);
			Conditionals condCheck = new Conditionals(new String[]{});

			if (condCheck.isCheckmate(board.getBoardData(), nextTurnIsWhite)) {
				System.out.println("CHECKMATE!");
				System.out.println((nextTurnIsWhite ? "Black" : "White") + " wins.");
				break;
			}

			if (condCheck.isStalemate(board.getBoardData(), nextTurnIsWhite)) {
				System.out.println("STALEMATE! Draw.");
				break;
			}

			if (condCheck.isKingInCheck(board.getBoardData(), nextTurnIsWhite)) {
				System.out.println("CHECK!");
			}
        }

        scanner.close();
    }
}

