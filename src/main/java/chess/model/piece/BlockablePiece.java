package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.Move;

public abstract class BlockablePiece extends Piece {

  protected BlockablePiece(PieceColor color, PieceType type) {
    super(color, type);
  }

  protected void computeValidMoves(int row, int col, int[][] directions, ChessBoard board) {
    board.validateBounds(row, col);
    validMoves.clear();

    for (int[] dir : directions) {
      int dist = 1;
      while (dist < board.getBoardSize()) {
        int destRow = row + dist * dir[0];
        int destCol = col + dist * dir[1];
        if (!board.isInBounds(destRow, destCol)) break;

        IPiece destPiece = board.getPieceAt(destRow, destCol);
        if (destPiece != null) {
          if (destPiece.getColor() != this.color) {
            validMoves.add(new Move(destRow, destCol)); // capture an enemy piece
          }
          break; // blocked by friendly piece
        }

        validMoves.add(new Move(destRow, destCol));
        dist++; // expand in this direction
      }
    }
  }
}
