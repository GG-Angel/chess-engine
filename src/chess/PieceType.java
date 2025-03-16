package chess;

public enum PieceType {
  W_PAWN, W_KNIGHT, W_BISHOP, W_ROOK, W_QUEEN, W_KING,
  B_PAWN, B_KNIGHT, B_BISHOP, B_ROOK, B_QUEEN, B_KING;

  public final static int NUM_OF_PIECE_BITBOARDS = PieceType.values().length;

  public static PieceType[] getAllPieceTypes() {
    return PieceType.values();
  }

  public static boolean isWhite(PieceType pieceType) {
    return pieceType.ordinal() < 6;
  }
}
