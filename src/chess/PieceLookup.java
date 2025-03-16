package chess;

import static chess.PieceType.*;

import java.util.HashMap;
import java.util.Map;

public class PieceLookup {
  private static final Map<Character, PieceType> LOOKUP_TYPE = new HashMap<>();
  private static final char[] LOOKUP_SYMBOL;
  private static final char[] LOOKUP_ASCII;

  // converts enum ordinals to piece characters
  static {
    LOOKUP_SYMBOL = new char[]{'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k'};
    LOOKUP_ASCII  = new char[]{'♙', '♘', '♗', '♖', '♕', '♔', '♟', '♞', '♝', '♜', '♛', '♚'};
  }

  // converts symbols to piece types for fen
  static {
    LOOKUP_TYPE.put('P', W_PAWN);
    LOOKUP_TYPE.put('N', W_KNIGHT);
    LOOKUP_TYPE.put('B', W_BISHOP);
    LOOKUP_TYPE.put('R', W_ROOK);
    LOOKUP_TYPE.put('Q', W_QUEEN);
    LOOKUP_TYPE.put('K', W_KING);

    LOOKUP_TYPE.put('p', B_PAWN);
    LOOKUP_TYPE.put('n', B_KNIGHT);
    LOOKUP_TYPE.put('b', B_BISHOP);
    LOOKUP_TYPE.put('r', B_ROOK);
    LOOKUP_TYPE.put('q', B_QUEEN);
    LOOKUP_TYPE.put('k', B_KING);
  }

  public static PieceType getPieceFromSymbol(char symbol) {
    return LOOKUP_TYPE.get(symbol);
  }

  public static char getTypeSymbol(PieceType pieceType) {
    return LOOKUP_SYMBOL[pieceType.ordinal()];
  }

  public static char getColorSymbol(PieceType pieceType) {
    return isWhite(pieceType) ? 'w' : 'b';
  }

  public static String getPieceImagePath(PieceType pieceType) {
    return "/pieces/" + getColorSymbol(pieceType) + getTypeSymbol(pieceType) + ".png";
  }
}
