package chess.model.piece;

public class ChessBishop extends SlidingPiece {

  public ChessBishop(PieceColor color, int position) {
    super(color, PieceType.BISHOP, position);
    this.hasMoved = !isAtInitialPosition(58, 2) && !isAtInitialPosition(61, 5);
  }
}
