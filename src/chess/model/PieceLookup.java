package chess.model;

public class PieceLookup {
  private static final String[][] LOOKUP = new String[2][6];

  static {
    LOOKUP[0] = new String[]{"P", "N", "B", "R", "Q", "K"}; // white
    LOOKUP[1] = new String[]{"p", "n", "b", "r", "q", "k"}; // black
  }

  public static String get(PieceColor color, PieceType type) {
    return LOOKUP[color.ordinal()][type.ordinal()];
  }
}
