package chess;

import static chess.Board.bitboardToString;

public class Masks {
  public static final long universe = 0xFFFFFFFFFFFFFFFFL;
  public static final long empty = 0L;

  public static final long rank8  = 0xFF00000000000000L;
  public static final long rank5  = 0xFF00000000L;
  public static final long rank4  = 0xFF000000L;
  public static final long rank1  = 0xFFL;
  public static final long fileA  = 0x8080808080808080L;
  public static final long fileAB = 0xC0C0C0C0C0C0C0C0L;
  public static final long fileGH = 0x0303030303030303L;
  public static final long fileH  = 0x0101010101010101L;

  public static final long whiteKSRook = 0x0101010101010101L;
  public static final long whiteQSRook = 0x1L;
  public static final long whiteKSInter = 0x06L;
  public static final long whiteQSInter = 0x7L;

  public static void main(String[] args) {
    System.out.println(bitboardToString(whiteKSRook));
  }

  public static final long knightMask = 0xA1100110AL;
  public static final long kingMask = 0x70507;

  // access by piece square for corresponding mask
  public static final long[] fileMasks = new long[64]; // files
  public static final long[] rankMasks = new long[64]; // ranks
  public static final long[] diagonalLeftMasks = new long[64];  // northwest-southeast
  public static final long[] diagonalRightMasks = new long[64]; // northeast-southwest

  // precompute sliding move masks
  static {
    for (int square = 0; square < 64; square++) {
      fileMasks[square] = computeStraightMask(square, 8) | computeStraightMask(square, -8);
      rankMasks[square] = computeStraightMask(square, 1) | computeStraightMask(square, -1);
      diagonalLeftMasks[square] = computeDiagonalMask(square, 7) | computeDiagonalMask(square, -7);
      diagonalRightMasks[square] = computeDiagonalMask(square, 9) | computeDiagonalMask(square, -9);
    }
  }

  private static long computeStraightMask(int square, int direction) {
    long mask = 0L;
    int currentSquare = square;

    while (true) {
      int nextSquare = currentSquare + direction;

      if (nextSquare < 0 || nextSquare >= 64) {
        break;
      }

      if ((direction == 1 || direction == -1) && (nextSquare / 8 != currentSquare / 8)) {
        break;
      }

      mask |= 1L << nextSquare;
      currentSquare = nextSquare;
    }

    return mask;
  }

  private static long computeDiagonalMask(int square, int direction) {
    long mask = 0L;
    int currentSquare = square;

    while (true) {
      int file = currentSquare % 8;
      int nextSquare = currentSquare + direction;
      int nextFile = nextSquare % 8;

      if (nextSquare < 0 || nextSquare >= 64 || Math.abs(nextFile - file) != 1) {
        break;
      }

      mask |= 1L << nextSquare;
      currentSquare = nextSquare;
    }

    return mask;
  }
}
