package chess.model;

import chess.model.piece.Piece;

public interface Board {

  Piece getPieceAtPosition(int position);

  int getEnPassantTarget();

  void setEnPassantTarget(int position);
}
