package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.Move;

import java.util.List;

public class ChessQueen extends DirectionalPiece {

  public ChessQueen(PieceColor color, int row, int col) {
    super(color, PieceType.QUEEN, row, col);
  }

  @Override
  public List<Move> computeMoves(Board board) {
    int[][] directions = new int[][] {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1},  // diagonal
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // straight
    };
    return computeMoves(directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♕" : "♛";
  }
}
