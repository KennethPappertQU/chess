//LoadGame.java

package saveload;

import models.CoolBoard;
import game.GameReplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoadGame {

    public void loadGameFile(File file, CoolBoard board, GameReplay replay) {

        try (Scanner scanner = new Scanner(file)) {

            String[][] boardData = board.getBoardData();
            String[][] pastMovements = replay.pastMovements;

            // Expect "BOARD"
            if (!scanner.next().equals("BOARD")) {
                throw new RuntimeException("Invalid save file: missing BOARD section");
            }

            // Load board
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boardData[i][j] = scanner.next();
                }
            }

            // Expect "MOVES"
            if (!scanner.next().equals("MOVES")) {
                throw new RuntimeException("Invalid save file: missing MOVES section");
            }

            // Load moves
            int index = 0;
            while (scanner.hasNext()) {
                String token = scanner.next();

                if (token.equals("END")) break;

                String from = token;
                String to = scanner.next();

                pastMovements[index][0] = from;
                pastMovements[index][1] = to;
                index++;
            }

            System.out.println("Game loaded successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("Save file not found: " + e.getMessage());
        }
    }
}