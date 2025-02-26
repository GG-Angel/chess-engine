package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.Move;

public class ChessPawn extends Piece {

  public ChessPawn(PieceColor color) {
    super(color, PieceType.PAWN);
  }

  @Override
  public void computeValidMoves(int row, int col, ChessBoard board) {
    board.validateBounds(row, col);
    validMoves.clear();

    int orientation = this.color == PieceColor.WHITE ? 1 : -1;
    int destRow = row + orientation;

    for (int direction = -1; direction <= 1; direction++) {
      int destCol = col + direction;
      if (!board.isInBounds(destRow, destCol)) continue;

      IPiece destPiece = board.getPieceAt(destRow, destCol);
      if ((direction == 0 && destPiece == null) || // move forward
          (direction != 0 && destPiece != null && destPiece.getColor() != this.color)) { // capture an enemy piece
        Move move = new Move(destRow, destCol);
        validMoves.add(move);
      }
    }

    // TODO: Implement en passant.
  }
}
