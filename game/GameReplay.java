//GameReplay.java

package SER120.chess.game;

import java.util.Scanner;
import SER120.chess.models.CoolBoard;

public class GameReplay {

    public String[][] pastMovements;
    private CoolBoard replayBoard;

    public GameReplay() {
        replayBoard = new CoolBoard(8, 8);
    }

    public void saveMovement(String[][] pastMovements, String fromPlace, String toPlace) {
        for (int i = 0; i < pastMovements.length; i++) {
            if (pastMovements[i][0] == null) {
                pastMovements[i][0] = fromPlace;
                pastMovements[i][1] = toPlace;
                return;
            }
        }
    }

    public void replaySaves(String[][] pastMovements) throws InterruptedException {
        Scanner in = new Scanner(System.in);

        // Reset board to starting position
        replayBoard.initializeBoard();

        // Play all moves forward
        for (int i = 0; i < pastMovements.length; i++) {
            if (pastMovements[i][0] == null) break;
            replayBoard.movePiece(pastMovements[i][0], pastMovements[i][1]);
        }

        // Step backward through moves
        for (int i = pastMovements.length - 1; i >= 0; i--) {
            if (pastMovements[i][0] == null) continue;

            replayBoard.showBoard();
            System.out.println("Stop at this move? y or n");

            String answer = in.nextLine();

            if (answer.equalsIgnoreCase("y")) {
                System.out.println("Loading...");
                Thread.sleep(1000);

                replayBoard.movePiece(pastMovements[i][0], pastMovements[i][1]);
                System.out.println("Piece moved.");
            } else {
                break;
            }
        }
    }
}

