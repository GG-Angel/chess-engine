package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import java.util.ArrayList;

public abstract class OddPiece extends ChessPiece {
  protected OddPiece(PieceColor color, PieceType type) throws NullPointerException {
    super(color, type);
  }

  protected void computeValidMoves(int fromRow, int fromCol, int[][] distances, ChessBoard board) throws IndexOutOfBoundsException {
    board.validateBounds(fromRow, fromCol);
    validMoves = new ArrayList<>();

    for (int[] dist : distances) {
      int destRow = fromRow + dist[0];
      int destCol = fromCol + dist[1];
      if (!board.isInBounds(destRow, destCol)) {
        continue;
      }

      Piece destPiece = board.getPieceAt(destRow, destCol);
      if (destPiece == null || destPiece.getColor() != this.color) {
        ChessMove move = new ChessMove(fromRow, fromCol, this, destRow, destCol, destPiece);
        validMoves.add(move);
      }
    }
  }
}
