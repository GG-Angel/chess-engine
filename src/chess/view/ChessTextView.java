package chess.view;

import chess.model.board.Board;
import chess.model.piece.abstracts.Piece;

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
    int size = board.getBoardSize();
    for (int row = 0; row < size; row++) {
      StringBuilder rowSB = new StringBuilder();
      for (int col = 0; col < size; col++) {
        Piece piece = board.getPieceAt(row, col);
        rowSB.append(piece != null ? piece.toString() : emptySlot);
        if (col < size - 1) {
          rowSB.append(' ');
        }
      }
      boardSB.append(rowSB);
      if (row < size - 1) {
        boardSB.append('\n');
      }
    }
    return boardSB.toString();
  }
}
