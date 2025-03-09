package chess.view;

import static chess.model.board.ChessBoard.BOARD_SIZE;

import chess.model.board.Board;
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
    for (int row = 0; row < BOARD_SIZE; row++) {
      StringBuilder rowSB = new StringBuilder();
      for (int col = 0; col < BOARD_SIZE; col++) {
        Piece piece = board.getPieceAt(row, col);
        rowSB.append(piece != null ? piece.toString() : emptySlot);
        if (col < BOARD_SIZE - 1) {
          rowSB.append(' ');
        }
      }
      boardSB.append(rowSB);
      if (row < BOARD_SIZE - 1) {
        boardSB.append('\n');
      }
    }
    return boardSB.toString();
  }
}
