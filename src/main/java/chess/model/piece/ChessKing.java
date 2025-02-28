package chess.model.piece;

import chess.model.board.ChessBoard;

public class ChessKing extends ChessPiece {

  public ChessKing(PieceColor color) {
    super(color, PieceType.KING);
  }

  @Override
  public void computeValidMoves(int fromRow, int fromCol, ChessBoard board) {
    
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
