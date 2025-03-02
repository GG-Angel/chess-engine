package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessKing extends ProximityPiece {

  public ChessKing(PieceColor color) {
    super(color, PieceType.KING);
  }

  @Override
  public void computeMoves(int fromRow, int fromCol, ChessBoard board) throws IndexOutOfBoundsException {
    // compute moves before checks
    int[][] distances = new int[][] {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
    computeMoves(fromRow, fromCol, distances, board);

    // prune moves that would walk into a check
    for (Piece piece : board.getOpposingPieces(this.color)) {
      this.validMoves.removeIf(move ->
          piece.getValidMoves().stream().anyMatch(move::collidesWith)
      );
    }
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
