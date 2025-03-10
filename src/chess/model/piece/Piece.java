package chess.model.piece;

public interface Piece {

  PieceColor getColor();

  PieceType getType();

  int getPosition();

  void setPosition(int position);
}
