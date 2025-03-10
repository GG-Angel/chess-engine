package chess.model;

public interface Board {

  Piece getPieceAtPosition(int position);

  Piece getPieceAtPosition(int x, int y);
}
