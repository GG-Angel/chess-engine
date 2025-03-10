package chess.view;

import static chess.model.ChessBoard.BOARD_SIZE;
import static chess.model.ChessBoard.to1D;

import chess.model.Board;
import chess.model.piece.Piece;

public class ChessTextView extends ChessView {
  private final char emptySlot = System.getProperty("os.name").equals("Mac OS X") ? '_' : '＿';

  public ChessTextView(Board board) throws NullPointerException {
    super(board);
  }

  public ChessTextView(Board board, Appendable appendable) throws NullPointerException {
    super(board, appendable);
  }

  @Override
  public String toString() {
    StringBuilder boardSB = new StringBuilder();
    for (int y = 0; y < BOARD_SIZE; y++) { // iterate over rows
      StringBuilder rowSB = new StringBuilder();
      for (int x = 0; x < BOARD_SIZE; x++) { // iterate over columns
        Piece piece = board.getPieceAtPosition(to1D(x, y));
        rowSB.append(piece != null ? piece.toString() : emptySlot);
        if (x < BOARD_SIZE - 1) {
          rowSB.append(' ');
        }
      }
      boardSB.append(rowSB);
      if (y < BOARD_SIZE - 1) {
        boardSB.append('\n');
      }
    }
    return boardSB.toString();
  }
}
