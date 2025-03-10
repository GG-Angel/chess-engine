package chess.model.piece;

import chess.model.ChessPiece;
import chess.model.PieceColor;
import chess.model.PieceType;

public class ChessKnight extends ChessPiece {

  public ChessKnight(PieceColor color, int x, int y) {
    super(color, PieceType.KNIGHT, x, y);
  }
}
