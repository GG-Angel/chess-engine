package chess.model.piece;

import chess.model.ChessPiece;
import chess.model.PieceColor;
import chess.model.PieceType;

public class ChessKing extends ChessPiece {

  public ChessKing(PieceColor color, int x, int y) {
    super(color, PieceType.KING, x, y);
  }
}
