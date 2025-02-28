package chess.view;

import chess.model.board.ChessBoard;
import chess.model.piece.IPiece;

public class ChessTextView extends ChessView {

  public ChessTextView(ChessBoard board) throws IllegalArgumentException {
    super(board);
  }

  public ChessTextView(ChessBoard board, Appendable appendable) throws IllegalArgumentException {
    super(board, appendable);
  }

  @Override
  public String toString() {
    StringBuilder boardSB = new StringBuilder();
    int size = board.getBoardSize();
    for (int row = 0; row < size; row++) {
      StringBuilder rowSB = new StringBuilder();
      for (int col = 0; col < size; col++) {
        IPiece piece = board.getPieceAt(row, col);
        rowSB.append(piece != null ? piece.toString() : '＿');
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
