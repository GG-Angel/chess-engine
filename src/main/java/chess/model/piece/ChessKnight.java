package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;

public class ChessKnight extends ChessPiece {
  public ChessKnight(PieceColor color) {
    super(color, PieceType.KNIGHT);
  }

  @Override
  public void computeValidMoves(int fromRow, int fromCol, ChessBoard board) {
    board.validateBounds(fromRow, fromCol);
    validMoves.clear();

    int[][] distances = new int[][] {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    for (int[] dist : distances) {
      int destRow = fromRow + dist[0];
      int destCol = fromCol + dist[1];
      if (!board.isInBounds(destRow, destCol)) {
        continue;
      }

      Piece destPiece = board.getPieceAt(destRow, destCol);
      if (destPiece == null || destPiece.getColor() != this.color) {
        ChessMove move = new ChessMove(fromRow, fromCol, destRow, destCol);
        validMoves.add(move);
      }
    }
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♘" : "♞";
  }
}
