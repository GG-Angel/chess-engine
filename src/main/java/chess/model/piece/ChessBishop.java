package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessBishop extends BlockablePiece {

  public ChessBishop(PieceColor color) {
    super(color, PieceType.BISHOP);
  }

  @Override
  public void computeValidMoves(int row, int col, ChessBoard board) {
    int[][] directions = new int[][] { {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };
    computeValidMoves(row, col, directions, board);
  }
}
