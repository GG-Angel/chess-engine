package chess;

import static chess.PieceLookup.getPieceImagePath;
import static chess.Utilities.toSquarePosition;
import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class ChessGUI extends JFrame {
  private static final int BOARD_SIZE = 8;
  private static final int SQUARE_SIZE = 56;
  private static final int PIECE_SIZE = 52;
  private static final int DIMENSIONS = BOARD_SIZE * SQUARE_SIZE;
  private static final Color C_BEIGE = new Color(240, 218, 181);
  private static final Color C_BROWN = new Color(181, 135, 99);

  private final ChessBoard board;
  private JPanel boardPanel;

  public ChessGUI(ChessBoard board) {
    this.board = board;
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

    Piece[] pieces = board.getBoard();
    for (int rank = 0; rank < BOARD_SIZE; rank++) {
      for (int file = 0; file < BOARD_SIZE; file++) {
        boolean isLightSquare = (rank + file) % 2 == 0;
        int square = toSquarePosition(rank, file);
        Piece piece = pieces[square];

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));

        JLabel squareLabel = new JLabel();
        squareLabel.setOpaque(true);
        squareLabel.setBackground(isLightSquare ? C_BEIGE : C_BROWN);
        squareLabel.setBounds(0, 0, SQUARE_SIZE, SQUARE_SIZE);
        layeredPane.add(squareLabel, JLayeredPane.DEFAULT_LAYER);

        if (piece != null) {
          String pieceImagePath = getPieceImagePath(piece);
          ImageIcon unscaledPieceIcon = new ImageIcon(requireNonNull(getClass().getResource(pieceImagePath)));
          Image scaledPieceImage = unscaledPieceIcon.getImage().getScaledInstance(PIECE_SIZE, PIECE_SIZE, Image.SCALE_SMOOTH);
          ImageIcon scaledPieceIcon = new ImageIcon(scaledPieceImage);

          JLabel pieceLabel = new JLabel(scaledPieceIcon);
          pieceLabel.setBounds(0, 0, SQUARE_SIZE, SQUARE_SIZE);
          layeredPane.add(pieceLabel, JLayeredPane.PALETTE_LAYER);
        }

        JLabel coordinateLabel = new JLabel(String.valueOf(square));
        coordinateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        coordinateLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        coordinateLabel.setForeground(Color.BLACK);
        coordinateLabel.setBounds(2, -6, SQUARE_SIZE, SQUARE_SIZE);
        layeredPane.add(coordinateLabel, JLayeredPane.MODAL_LAYER);

        boardPanel.add(layeredPane);
      }
    }

    boardPanel.revalidate();
    boardPanel.repaint();
  }
}
