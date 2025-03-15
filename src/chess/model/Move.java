package chess.model;

import chess.model.piece.Piece;

public interface Move {
  int getFrom();

  int getTo();

  Piece getPiece();

  MoveType getMoveType();

  boolean hasPieceMovedBefore();

  Piece getPromotionPiece() throws IllegalStateException;

  Move getCastlingMove() throws IllegalStateException;

  int getEnPassantPawnPosition() throws IllegalStateException;
}
