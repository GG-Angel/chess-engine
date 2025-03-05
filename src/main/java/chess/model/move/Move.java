package chess.model.move;

import chess.model.piece.Piece;

public interface Move {

  boolean collidesWith(Move other);

  boolean threatensKing();

  boolean wasFirstMove();

  Move getSubMove();

  ChessMoveType getMoveType();

  int fromRow();

  int fromCol();

  Piece fromPiece();

  int toRow();

  int toCol();

  Piece toPiece();

}
