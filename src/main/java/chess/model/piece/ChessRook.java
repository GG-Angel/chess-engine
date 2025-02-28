package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessRook extends BlockablePiece {

  public ChessRook(PieceColor color) {
    super(color, PieceType.ROOK);
  }

  @Override
  public void computeValidMoves(int fromRow, int fromCol, ChessBoard board) {
    int[][] directions = new int[][] { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
    computeValidMoves(fromRow, fromCol, directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♖" : "♜";
  }
}
