package chess;

import static chess.Utilities.toPosition;
import static java.util.Objects.requireNonNull;

import java.util.Objects;
import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {
  private static final int BOARD_SIZE = 8;
  private static final int SQUARE_SIZE = 64;
  private static final int DIMENSIONS = BOARD_SIZE * SQUARE_SIZE;
  private static final Color C_BEIGE = new Color(240, 218, 181);
  private static final Color C_BROWN = new Color(181, 135, 99);

  private JPanel boardPanel;
  private ChessBoard chessBoard;

  public ChessGUI() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessGUI(String fen) {
    this.chessBoard = new ChessBoard(fen);
    initialize();
  }

  private void initialize() {
    setTitle("Chess Game");
    setSize(DIMENSIONS, DIMENSIONS);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setResizable(false);

    boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
    add(boardPanel, BorderLayout.CENTER);

    render();
  }

  private void render() {
    boardPanel.removeAll();
    char[] board = chessBoard.toString().replaceAll("\\s+", "").toCharArray();
    for (int rank = 0; rank < BOARD_SIZE; rank++) {
      for (int file = 0; file < BOARD_SIZE; file++) {
        char pieceSymbol = board[toPosition(rank, file)];
        boolean isLightSquare = (rank + file) % 2 == 0;

        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(isLightSquare ? C_BEIGE : C_BROWN);

        if (pieceSymbol != '.') {
          PieceType pieceType = PieceLookup.getPieceFromSymbol(pieceSymbol);
          String pieceImagePath = PieceLookup.getPieceImagePath(pieceType);
          ImageIcon pieceIcon = new ImageIcon(requireNonNull(getClass().getResource(pieceImagePath)));
          Image pieceImage = pieceIcon.getImage().getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
          label.setIcon(new ImageIcon(pieceImage));
        }

        boardPanel.add(label);
      }
    }

    boardPanel.revalidate();
    boardPanel.repaint();
  }

  public static void main(String[] args) {
    ChessGUI gui = new ChessGUI();
    gui.setVisible(true);
  }
}
