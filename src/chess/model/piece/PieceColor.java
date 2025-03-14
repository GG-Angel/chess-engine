package chess.model.piece;

public enum PieceColor {
  WHITE, BLACK;

  public static PieceColor getEnemyColor(PieceColor color) {
    return color == WHITE ? BLACK : WHITE;
  }
}
