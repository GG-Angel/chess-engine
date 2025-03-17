package chess;

import static chess.Utilities.toSquarePosition;

public class BitBoard {
  public static long setBit(long bitboard, int square) {
    return bitboard | (1L << square);
  }

  public static long clearBit(long bitboard, int square) {
    return bitboard & ~(1L << square);
  }

  public static long toggleBit(long bitboard, int square) {
    return bitboard ^ (1L << square);
  }

  public static boolean hasPieceAtBit(long bitboard, int square) {
    return (bitboard & (1L << square)) != 0;
  }

  public static String toString(long bitboard) {
    StringBuilder sbBoard = new StringBuilder();
    for (int rank = 0; rank < 8; rank++) {
      for (int file = 0; file < 8; file++) {
        int square = toSquarePosition(rank, file);
        if (hasPieceAtBit(bitboard, square)) {
          sbBoard.append("1");
        } else {
          sbBoard.append("-");
        }
        if (file < 7) {
          sbBoard.append(' ');
        }
      }
      sbBoard.append('\n');
    }
    return sbBoard.toString();
  }
}
