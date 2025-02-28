package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessBishop extends BlockablePiece {

  public ChessBishop(PieceColor color) {
    super(color, PieceType.BISHOP);
  }

  @Override
  public void computeValidMoves(int fromRow, int fromCol, ChessBoard board) {
    int[][] directions = new int[][] { {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };
    computeValidMoves(fromRow, fromCol, directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♗" : "♝";
  }
}
