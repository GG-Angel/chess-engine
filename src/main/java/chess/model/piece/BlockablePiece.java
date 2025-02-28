package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import java.util.ArrayList;

public abstract class BlockablePiece extends ChessPiece {

  protected BlockablePiece(PieceColor color, PieceType type) throws NullPointerException {
    super(color, type);
  }

  protected void computeValidMoves(int fromRow, int fromCol, int[][] directions, ChessBoard board) throws IndexOutOfBoundsException {
    board.validateBounds(fromRow, fromCol);
    validMoves = new ArrayList<>();

    for (int[] dir : directions) {
      int dist = 1;
      while (dist < board.getBoardSize()) {
        int destRow = fromRow + dist * dir[0];
        int destCol = fromCol + dist * dir[1];
        if (!board.isInBounds(destRow, destCol)) break;

        Piece destPiece = board.getPieceAt(destRow, destCol);
        if (destPiece != null) {
          if (destPiece.getColor() != this.color) {
            // capture an enemy piece
            validMoves.add(new ChessMove(fromRow, fromCol, this, destRow, destCol, destPiece));
          }
          break; // blocked by friendly or enemy piece
        }

        validMoves.add(new ChessMove(fromRow, fromCol, this, destRow, destCol, null));
        dist++; // expand in this direction
      }
    }
  }
}
