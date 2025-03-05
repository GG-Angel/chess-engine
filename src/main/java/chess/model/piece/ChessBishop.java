package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.Move;

import java.util.List;

public class ChessBishop extends DirectionalPiece {

  public ChessBishop(PieceColor color) {
    super(color, PieceType.BISHOP);
  }

  @Override
  public List<Move> computeMoves(int fromRow, int fromCol, ChessBoard board) {
    int[][] directions = new int[][] { {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };
    return computeMoves(fromRow, fromCol, directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♗" : "♝";
  }
}
