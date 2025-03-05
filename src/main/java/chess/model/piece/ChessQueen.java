package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.Move;

import java.util.List;

public class ChessQueen extends DirectionalPiece {

  public ChessQueen(PieceColor color) {
    super(color, PieceType.QUEEN);
  }

  @Override
  public List<Move> computeMoves(int fromRow, int fromCol, Board board) {
    int[][] directions = new int[][] {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1},  // diagonal
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // straight
    };
    return computeMoves(fromRow, fromCol, directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♕" : "♛";
  }
}
