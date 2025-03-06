package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.Move;

import java.util.List;

public class ChessKnight extends ProximityPiece {

  public ChessKnight(PieceColor color, int row, int col) {
    super(color, PieceType.KNIGHT, row, col);
  }

  @Override
  public List<Move> computeMoves(Board board) throws IndexOutOfBoundsException {
    int[][] distances = new int[][] {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    return computeMoves(distances, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♘" : "♞";
  }
}
