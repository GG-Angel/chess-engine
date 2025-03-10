package chess.model.piece;

import static chess.model.piece.PieceType.*;

import java.util.HashMap;
import java.util.Map;

public class PieceLookup {
  private static final String[][] LOOKUP_SYMBOL = new String[2][6];
  private static final String[][] LOOKUP_ASCII = new String[2][6];
  private static final Map<Character, PieceType> LOOKUP_TYPE = new HashMap<>();

  // converts enum ordinals to piece symbol/ascii characters
  static {
    LOOKUP_SYMBOL[0] = new String[]{"P", "N", "B", "R", "Q", "K"}; // white
    LOOKUP_SYMBOL[1] = new String[]{"p", "n", "b", "r", "q", "k"}; // black
    LOOKUP_ASCII[0]  = new String[]{"♙", "♘", "♗", "♖", "♕", "♔"}; // white
    LOOKUP_ASCII[1]  = new String[]{"♟", "♞", "♝", "♜", "♛", "♚"}; // black
  }

  // converts symbols to piece types for fen
  static {
    LOOKUP_TYPE.put('p', PAWN);
    LOOKUP_TYPE.put('n', KNIGHT);
    LOOKUP_TYPE.put('b', BISHOP);
    LOOKUP_TYPE.put('r', ROOK);
    LOOKUP_TYPE.put('q', QUEEN);
    LOOKUP_TYPE.put('k', KING);
  }

  public static String getSymbol(PieceColor color, PieceType type) {
    return LOOKUP_SYMBOL[color.ordinal()][type.ordinal()];
  }

  public static String getASCII(PieceColor color, PieceType type) {
    return LOOKUP_ASCII[color.ordinal()][type.ordinal()];
  }

  public static PieceType getType(Character symbol) {
    return LOOKUP_TYPE.get(Character.toLowerCase(symbol));
  }
}
