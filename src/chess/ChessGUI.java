package chess;

import static chess.PieceLookup.getPieceImagePath;
import static chess.Utilities.toSquarePosition;
import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class ChessGUI extends JFrame {
  private static final int BOARD_SIZE = 8;
  private static final int SQUARE_SIZE = 64;
  private static final int DIMENSIONS = BOARD_SIZE * SQUARE_SIZE;
  private static final Color C_BEIGE = new Color(240, 218, 181);
  private static final Color C_BROWN = new Color(181, 135, 99);

  private final ChessBoard board;
  private JPanel boardPanel;

  public ChessGUI() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessGUI(String fen) {
    this.board = new ChessBoard(fen);
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

    PieceType[] pieces = board.getBoard();
    for (int rank = 0; rank < BOARD_SIZE; rank++) {
      for (int file = 0; file < BOARD_SIZE; file++) {
        boolean isLightSquare = (rank + file) % 2 == 0;
        int square = toSquarePosition(rank, file);
        PieceType piece = pieces[square];

        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(isLightSquare ? C_BEIGE : C_BROWN);

        if (piece != null) {
          String pieceImagePath = getPieceImagePath(piece);

          ImageIcon unscaledPieceIcon = new ImageIcon(requireNonNull(getClass().getResource(pieceImagePath)));
          Image scaledPieceImage = unscaledPieceIcon.getImage().getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
          ImageIcon scaledPieceIcon = new ImageIcon(scaledPieceImage);

          label.setIcon(scaledPieceIcon);
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
