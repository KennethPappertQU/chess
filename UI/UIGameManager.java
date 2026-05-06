package UI;

import java.io.File;

import models.CoolBoard;
import saveload.SaveGame;
import saveload.LoadGame;
import game.GameReplay;

import javax.swing.SwingUtilities;

public class UIGameManager {

    CoolBoard board;
    GameReplay replay;
    SaveGame saver;
    LoadGame loader;

    public UIGameManager() {
        this.board = new CoolBoard(8, 8);
        this.replay = new GameReplay();
        this.replay.pastMovements = new String[200][2];
        this.saver = new SaveGame();
        this.loader = new LoadGame();
    }

    public void runGame() {
        SwingUtilities.invokeLater(() ->
                new UI.ChessUI(board, replay, saver, loader));
    }

    public static void main(String[] args) {
        UIGameManager gm = new UIGameManager();
        gm.runGame();
    }
}
