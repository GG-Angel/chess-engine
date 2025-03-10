package chess.model.piece;

import chess.model.ChessPiece;
import chess.model.PieceColor;
import chess.model.PieceType;

public class ChessBishop extends ChessPiece {

  public ChessBishop(PieceColor color, int x, int y) {
    super(color, PieceType.BISHOP, x, y);
  }
}
