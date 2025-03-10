package chess.model.piece;

import chess.model.ChessPiece;
import chess.model.PieceColor;
import chess.model.PieceType;

public class ChessPawn extends ChessPiece {

  public ChessPawn(PieceColor color, int x, int y) {
    super(color, PieceType.PAWN, x, y);
  }
}
