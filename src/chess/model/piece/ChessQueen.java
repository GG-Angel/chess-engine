package chess.model.piece;

public class ChessQueen extends SlidingPiece {

  public ChessQueen(PieceColor color, int position) {
    super(color, PieceType.QUEEN, position);
    this.hasMoved = !isAtInitialPosition(59, 3);
  }
}
