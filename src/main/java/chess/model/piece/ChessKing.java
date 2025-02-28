package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessKing extends Piece {

  public ChessKing(PieceColor color) {
    super(color, PieceType.KING);
  }

  @Override
  public void computeValidMoves(int row, int col, ChessBoard board) {
    
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
