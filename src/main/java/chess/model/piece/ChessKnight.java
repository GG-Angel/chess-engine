package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessKnight extends ProximityPiece {
  public ChessKnight(PieceColor color) {
    super(color, PieceType.KNIGHT);
  }

  @Override
  public void computeMoves(int fromRow, int fromCol, ChessBoard board) throws IndexOutOfBoundsException {
    int[][] distances = new int[][] {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    computeMoves(fromRow, fromCol, distances, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♘" : "♞";
  }
}
