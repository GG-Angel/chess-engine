package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessBishop extends DirectionalPiece {

  public ChessBishop(PieceColor color) {
    super(color, PieceType.BISHOP);
  }

  @Override
  public void computeMoves(int fromRow, int fromCol, ChessBoard board) {
    int[][] directions = new int[][] { {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };
    computeMoves(fromRow, fromCol, directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♗" : "♝";
  }
}
