package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessQueen extends DirectionalPiece {

  public ChessQueen(PieceColor color) {
    super(color, PieceType.QUEEN);
  }

  @Override
  public void computeMoves(int fromRow, int fromCol, ChessBoard board) {
    int[][] directions = new int[][] {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1},  // diagonal
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // straight
    };
    computeMoves(fromRow, fromCol, directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♕" : "♛";
  }
}
