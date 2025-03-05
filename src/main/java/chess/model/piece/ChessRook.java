package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.Move;

import java.util.List;

public class ChessRook extends DirectionalPiece {

  public ChessRook(PieceColor color) {
    super(color, PieceType.ROOK);
  }

  @Override
  public List<Move> computeMoves(int fromRow, int fromCol, Board board) {
    int[][] directions = new int[][] { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
    return computeMoves(fromRow, fromCol, directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♖" : "♜";
  }
}
