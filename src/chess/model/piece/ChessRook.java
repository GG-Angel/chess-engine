package chess.model.piece;

import chess.model.ChessPiece;
import chess.model.PieceColor;
import chess.model.PieceType;

public class ChessRook extends ChessPiece {

  public ChessRook(PieceColor color, int x, int y) {
    super(color, PieceType.ROOK, x, y);
  }
}
