package chess.model.move;

import chess.model.piece.Piece;

public interface Move {

  int fromRow();

  int fromCol();

  Piece fromPiece();

  int toRow();

  int toCol();

  Piece toPiece();
}
