package UI;

import models.CoolBoard;
import game.Conditionals;
import saveload.SaveGame;
import saveload.LoadGame;
import game.GameReplay;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ChessUI extends JFrame {

    private final JButton[][] squares = new JButton[8][8];
    private final CoolBoard board;
    private final GameReplay replay;
    private final SaveGame saver;
    private final LoadGame loader;

    private String firstClick = null;
    private int round = 0;

    private final ArrayList<String> whiteCaptured = new ArrayList<>();
    private final ArrayList<String> blackCaptured = new ArrayList<>();

    private final JLabel whiteCapturedLabel = new JLabel();
    private final JLabel blackCapturedLabel = new JLabel();
    private final JLabel turnLabel = new JLabel("White to move");

    public ChessUI(CoolBoard board, GameReplay replay, SaveGame saver, LoadGame loader) {
        this.board = board;
        this.replay = replay;
        this.saver = saver;
        this.loader = loader;

        setTitle("Java Chess");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopBar(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createSidebarPanel(), BorderLayout.EAST);

        refreshBoard();
        updateSidebar();
        updateTurnLabel();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JComponent createTopBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        JButton replayBtn = new JButton("Replay");
        JButton exitBtn = new JButton("Exit");

        saveBtn.addActionListener(e -> {
            saver.saveGameFile(new File("save.txt"), board, replay);
            JOptionPane.showMessageDialog(this, "Game saved.");
        });

        loadBtn.addActionListener(e -> {
            loader.loadGameFile(new File("save.txt"), board, replay);
            // after load, reset UI state
            firstClick = null;
            round = recomputeRoundFromReplay();
            whiteCaptured.clear();
            blackCaptured.clear();
            recomputeCapturedFromBoard();
            refreshBoard();
            updateSidebar();
            updateTurnLabel();
            JOptionPane.showMessageDialog(this, "Game loaded.");
        });

        replayBtn.addActionListener(e -> {
            try {
                replay.replaySaves(replay.pastMovements);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Replay error: " + ex.getMessage());
            }
        });

        exitBtn.addActionListener(e -> dispose());

        panel.add(saveBtn);
        panel.add(loadBtn);
        panel.add(replayBtn);
        panel.add(exitBtn);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(turnLabel);

        return panel;
    }

    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 8));

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 32));

                Color light = new Color(240, 217, 181);
                Color dark = new Color(181, 136, 99);
                btn.setBackground((r + c) % 2 == 0 ? light : dark);

                final int row = r;
                final int col = c;
                btn.addActionListener(e -> handleClick(row, col));

                squares[r][c] = btn;
                panel.add(btn);
            }
        }

        return panel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 800));

        JLabel title = new JLabel("Captured Pieces");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel whiteTitle = new JLabel("White captured:");
        whiteTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel blackTitle = new JLabel("Black captured:");
        blackTitle.setFont(new Font("Arial", Font.BOLD, 16));

        whiteCapturedLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        blackCapturedLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(whiteTitle);
        sidebar.add(whiteCapturedLabel);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(blackTitle);
        sidebar.add(blackCapturedLabel);

        return sidebar;
    }

    private void handleClick(int row, int col) {
        String[][] data = board.getBoardData();
        String coord = convertToChessCoord(row, col);

        if (firstClick == null) {
            if (data[row][col].equals("--")) return;

            boolean whiteTurn = (round % 2 == 0);
            char color = data[row][col].charAt(0);

            if (whiteTurn && color != 'W') return;
            if (!whiteTurn && color != 'B') return;

            firstClick = coord;
        } else {
            String from = firstClick;
            String to = coord;

            Conditionals cond = new Conditionals(new String[]{});

            int fromCol = from.charAt(0) - 'A';
            int fromRow = 8 - (from.charAt(1) - '0');

            int toCol = to.charAt(0) - 'A';
            int toRow = 8 - (to.charAt(1) - '0');

            String piece = board.getPieceAt(fromRow, fromCol);

            if (piece.equals("--")) {
                firstClick = null;
                return;
            }

            boolean whiteTurn = (round % 2 == 0);
            boolean isWhitePiece = piece.charAt(0) == 'W';

            if (whiteTurn && !isWhitePiece) {
                firstClick = null;
                return;
            }
            if (!whiteTurn && isWhitePiece) {
                firstClick = null;
                return;
            }

            if (!cond.isValidFormat(from) || !cond.isValidFormat(to)) {
                firstClick = null;
                return;
            }

            if (!cond.illegalMovementCheck(piece, to, from, board.getBoardData())) {
                firstClick = null;
                return;
            }

            // capture detection
            String target = board.getPieceAt(toRow, toCol);
            if (!target.equals("--")) {
                if (target.charAt(0) == 'W') {
                    whiteCaptured.add(target);
                } else {
                    blackCaptured.add(target);
                }
            }

            // move piece (same logic as GameManager)
            board.setPieceAt(toRow, toCol, piece);
            board.setPieceAt(fromRow, fromCol, "--");

            // save to replay
            replay.saveMovement(replay.pastMovements, from, to);

            round++;
            firstClick = null;

            refreshBoard();
            updateSidebar();
            updateTurnLabel();
            checkGameState();
        }
    }

    private void checkGameState() {
        Conditionals condCheck = new Conditionals(new String[]{});
        boolean nextTurnIsWhite = (round % 2 == 0);

        if (condCheck.isCheckmate(board.getBoardData(), nextTurnIsWhite)) {
            JOptionPane.showMessageDialog(this,
                    "CHECKMATE! " + (nextTurnIsWhite ? "Black" : "White") + " wins.");
        } else if (condCheck.isStalemate(board.getBoardData(), nextTurnIsWhite)) {
            JOptionPane.showMessageDialog(this, "STALEMATE! Draw.");
        } else if (condCheck.isKingInCheck(board.getBoardData(), nextTurnIsWhite)) {
            JOptionPane.showMessageDialog(this, "CHECK!");
        }
    }

    private void refreshBoard() {
        String[][] data = board.getBoardData();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                String piece = data[r][c];
                squares[r][c].setText(piece.equals("--") ? "" : convertPieceToUnicode(piece));
            }
        }
    }

    private void updateSidebar() {
        whiteCapturedLabel.setText(convertListToUnicode(whiteCaptured));
        blackCapturedLabel.setText(convertListToUnicode(blackCaptured));
    }

    private void updateTurnLabel() {
        turnLabel.setText(round % 2 == 0 ? "White to move" : "Black to move");
    }

    private int recomputeRoundFromReplay() {
        int count = 0;
        if (replay.pastMovements == null) return 0;
        for (String[] mv : replay.pastMovements) {
            if (mv == null || mv[0] == null || mv[1] == null) break;
            count++;
        }
        return count;
    }

    private void recomputeCapturedFromBoard() {
        // optional: you can leave this empty or implement from initial setup
        // for now we just clear captured lists on load (already done)
    }

    private String convertListToUnicode(java.util.List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String p : list) sb.append(convertPieceToUnicode(p)).append(" ");
        return sb.toString();
    }

    private String convertPieceToUnicode(String piece) {
        switch (piece) {
            case "WP": return "♙";
            case "WR": return "♖";
            case "WN": return "♘";
            case "WB": return "♗";
            case "WQ": return "♕";
            case "WK": return "♔";
            case "BP": return "♟";
            case "BR": return "♜";
            case "BN": return "♞";
            case "BB": return "♝";
            case "BQ": return "♛";
            case "BK": return "♚";
        }
        return "";
    }

    private String convertToChessCoord(int row, int col) {
        char file = (char) ('A' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }
}