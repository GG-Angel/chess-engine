package chess.model;

public interface Piece {

  PieceColor getColor();

  PieceType getType();

  int getX();

  int getY();

  void setPosition(int x, int y);
}
