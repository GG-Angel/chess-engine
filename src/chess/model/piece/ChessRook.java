package chess.model.piece;

public class ChessRook extends SlidingPiece {

  public ChessRook(PieceColor color, int position) {
    super(color, PieceType.ROOK, position);
    this.hasMoved = !isAtInitialPosition(56, 0) && !isAtInitialPosition(63, 7);
  }
}
