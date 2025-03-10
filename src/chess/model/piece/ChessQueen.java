package chess.model.piece;

import chess.model.ChessPiece;
import chess.model.PieceColor;
import chess.model.PieceType;

public class ChessQueen extends ChessPiece {

  public ChessQueen(PieceColor color, int x, int y) {
    super(color, PieceType.QUEEN, x, y);
  }
}
