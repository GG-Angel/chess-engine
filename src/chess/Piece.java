package chess;

public enum Piece {
  W_PAWN, W_KNIGHT, W_BISHOP, W_ROOK, W_QUEEN, W_KING,
  B_PAWN, B_KNIGHT, B_BISHOP, B_ROOK, B_QUEEN, B_KING;

  public final static int NUM_OF_PIECE_BITBOARDS = Piece.values().length;

  public static Piece[] getAllPieceTypes() {
    return Piece.values();
  }

  public static boolean isWhite(Piece pieceType) {
    return pieceType.ordinal() < 6;
  }
}
