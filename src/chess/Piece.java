package chess;

import java.util.HashMap;
import java.util.Map;

public class Piece {
  public enum Type {
    PAWN   ('p', 0),
    KNIGHT ('n', 1),
    BISHOP ('b', 2),
    ROOK   ('r', 3),
    QUEEN  ('q', 4),
    KING   ('k', 5);

    public final char symbol;
    public final int index;

    Type(char symbol, int index) {
      this.symbol = symbol;
      this.index = index;
    }

    // initialize lookup table
    private static final Map<Character, Type> symbolToType = new HashMap<>();

    static {
      for (Type pieceType : values()) {
        symbolToType.put(pieceType.symbol, pieceType);
      }
    }

    public static Type fromChar(char symbol) {
      return symbolToType.get(Character.toLowerCase(symbol));
    }
  }

  public enum Color {
    WHITE (0),
    BLACK (6);

    public final int offset;

    Color(int offset) {
      this.offset = offset;
    }

    public static Color fromChar(char symbol) {
      return Character.isUpperCase(symbol) ? WHITE : BLACK;
    }
  }

  public static char getSymbol(Color color, Type type) {
    return color == Color.WHITE ? Character.toUpperCase(type.symbol) : type.symbol;
  }

  public static int getIndex(Color color, Type type) {
    return type.index + color.offset;
  }
}
