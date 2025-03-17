package chess;

import java.util.HashMap;
import java.util.Map;

public enum Piece {
  WHITE_PAWN   ('P', 0),
  WHITE_KNIGHT ('N', 1),
  WHITE_BISHOP ('B', 2),
  WHITE_ROOK   ('R', 3),
  WHITE_QUEEN  ('Q', 4),
  WHITE_KING   ('K', 5),
  BLACK_PAWN   ('p', 6),
  BLACK_KNIGHT ('n', 7),
  BLACK_BISHOP ('b', 8),
  BLACK_ROOK   ('r', 9),
  BLACK_QUEEN  ('q', 10),
  BLACK_KING   ('k', 11);

  private final char symbol;
  private final int index;

  private static final Map<Character, Piece> symbolToPiece = new HashMap<>();
  private static final Map<Piece, Character> pieceToSymbol = new HashMap<>();

  Piece(char symbol, int index) {
    this.symbol = symbol;
    this.index = index;
  }

  static {
    for (Piece piece : values()) {
      symbolToPiece.put(piece.symbol, piece);
      pieceToSymbol.put(piece, piece.symbol);
    }
  }

  public static Piece fromChar(char symbol) {
    return symbolToPiece.get(symbol);
  }

  public char toChar() {
    return pieceToSymbol.get(this);
  }

  public int index() {
    return index;
  }

  public static boolean isWhite(int piece) {
    return piece >= WHITE_PAWN.ordinal() && piece <= WHITE_KING.ordinal();
  }

  public static boolean isBlack(int piece) {
    return piece >= BLACK_PAWN.ordinal() && piece <= BLACK_KING.ordinal();
  }
}
