package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessKing extends OddPiece {

  public ChessKing(PieceColor color) {
    super(color, PieceType.KING);
  }

  @Override
  public void computeValidMoves(int fromRow, int fromCol, ChessBoard board) throws IndexOutOfBoundsException {
    // compute moves before checks
    int[][] distances = new int[][] {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
    computeValidMoves(fromRow, fromCol, distances, board);

    // prune moves that would walk into a check
    for (Piece piece : board.getLivingPieces()) {
      if (piece.getColor() != this.color) {
        this.validMoves.removeAll(piece.getValidMoves());
      }
    }
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
