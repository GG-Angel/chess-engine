package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;

public class ChessPawn extends ChessPiece {

  public ChessPawn(PieceColor color) {
    super(color, PieceType.PAWN);
  }

  @Override
  public void computeValidMoves(int fromRow, int fromCol, ChessBoard board) {
    board.validateBounds(fromRow, fromCol);
    validMoves.clear();

    int orientation = this.color == PieceColor.WHITE ? -1 : 1;
    int destRow = fromRow + orientation;
    for (int direction = -1; direction <= 1; direction++) {
      int destCol = fromCol + direction;
      if (!board.isInBounds(destRow, destCol)) continue;

      Piece destPiece = board.getPieceAt(destRow, destCol);
      if ((direction == 0 && destPiece == null) || // move forward
          (direction != 0 && destPiece != null
              && destPiece.getColor() != this.color)) { // capture an enemy piece
        ChessMove move = new ChessMove(fromRow, fromCol, this, destRow, destCol, destPiece);
        validMoves.add(move);

        // check for double jump
        if (direction == 0 && !hasMoved) {
          int destRowJump = destRow + orientation;
          if (!board.isInBounds(destRowJump, destCol)) continue;
          Piece destPieceJump = board.getPieceAt(destRowJump, destCol);
          if (destPieceJump == null || destPieceJump.getColor() != this.color) {
            ChessMove moveJump = new ChessMove(fromRow, fromCol, this, destRowJump, destCol, destPieceJump);
            validMoves.add(moveJump);
          }
        }
      }
    }

    // TODO: Implement en passant.
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♙" : "♟";
  }
}
