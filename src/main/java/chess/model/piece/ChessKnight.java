package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import java.util.ArrayList;

public class ChessKnight extends OddPiece {
  public ChessKnight(PieceColor color) {
    super(color, PieceType.KNIGHT);
  }

  @Override
  public void computeValidMoves(int fromRow, int fromCol, ChessBoard board) throws IndexOutOfBoundsException {
    int[][] distances = new int[][] {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    computeValidMoves(fromRow, fromCol, distances, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♘" : "♞";
  }
}
