package chess;

public enum Piece {
  WHITE_PAWN,
  WHITE_KNIGHT,
  WHITE_BISHOP,
  WHITE_ROOK,
  WHITE_QUEEN,
  WHITE_KING,
  BLACK_PAWN,
  BLACK_KNIGHT,
  BLACK_BISHOP,
  BLACK_ROOK,
  BLACK_QUEEN,
  BLACK_KING;

  public static boolean isWhite(int piece) {
    return piece >= WHITE_PAWN.ordinal() && piece <= WHITE_KING.ordinal();
  }

  public static boolean isBlack(int piece) {
    return piece >= BLACK_PAWN.ordinal() && piece <= BLACK_KING.ordinal();
  }
}
