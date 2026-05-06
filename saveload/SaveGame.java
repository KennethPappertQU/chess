//SaveGame.java

package saveload;

import models.CoolBoard;
import game.GameReplay;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveGame {

    public void saveGameFile(File saveFile, CoolBoard board, GameReplay replay) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(saveFile))) {

            String[][] boardData = board.getBoardData();
            String[][] pastMovements = replay.pastMovements;

            writer.println("BOARD");
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    writer.print(boardData[i][j] + " ");
                }
                writer.println();
            }

            writer.println("MOVES");
            for (int i = 0; i < pastMovements.length; i++) {
                if (pastMovements[i][0] == null) break;
                writer.println(pastMovements[i][0] + " " + pastMovements[i][1]);
            }

            writer.println("END");

            System.out.println("Game saved successfully to: " + saveFile.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }
}
