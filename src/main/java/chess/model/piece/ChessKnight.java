package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.Move;

public class ChessKnight extends Piece {
  public ChessKnight(PieceColor color) {
    super(color, PieceType.KNIGHT);
  }

  @Override
  public void computeValidMoves(int row, int col, ChessBoard board) {
    board.validateBounds(row, col);
    validMoves.clear();

    int[][] distances = new int[][] {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    for (int[] dist : distances) {
      int destRow = row + dist[0];
      int destCol = col + dist[1];
      if (!board.isInBounds(destRow, destCol)) {
        continue;
      }

      IPiece destPiece = board.getPieceAt(destRow, destCol);
      if (destPiece == null || destPiece.getColor() != this.color) {
        Move move = new Move(destRow, destCol);
        validMoves.add(move);
      }
    }
  }
}
