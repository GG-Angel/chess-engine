package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.move.Move;

public class ChessKing extends ProximityPiece {

  public ChessKing(PieceColor color) {
    super(color, PieceType.KING);
  }

  @Override
  public void computeMoves(int fromRow, int fromCol, ChessBoard board) throws IndexOutOfBoundsException {
    // compute moves before checks
    int[][] distances = new int[][] {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
    computeMoves(fromRow, fromCol, distances, board);
    computeCastling(fromRow, fromCol, board); // TODO: Verify this avoid checks.

    // prune moves that would walk into a check
    for (Piece piece : board.getOpposingPieces(this.color)) {
      this.validMoves.removeIf(move ->
          piece.getValidMoves().stream().anyMatch(move::collidesWith)
      );
    }
  }

  private void computeCastling(int fromRow, int fromCol, ChessBoard board) {
    if (hasMoved) return;
    checkCastling(fromRow, fromCol, board, 7, 6, 5); // king-side castling
    checkCastling(fromRow, fromCol, board, 0, 2, 3); // queen-side castling
  }

  private void checkCastling(int fromRow, int fromCol, ChessBoard board, int rookCol, int kingTargetCol, int rookTargetCol) {
    Piece rook = board.getPieceAt(fromRow, rookCol);
    if (rook == null || rook.getType() != PieceType.ROOK || isOpposingPiece(rook) || rook.hasMoved()) return;

    int step = (rookCol == 7) ? 1 : -1;
    for (int col = fromCol + step; col != rookCol; col += step) {
      if (board.getPieceAt(fromRow, col) != null) return; // collision detected
    }

    Move rookMove = new ChessMove(fromRow, rookCol, rook, fromRow, rookTargetCol, null);
    Move kingMove = new ChessMove(fromRow, fromCol, this, fromRow, kingTargetCol, null, rookMove);
    validMoves.add(kingMove);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
