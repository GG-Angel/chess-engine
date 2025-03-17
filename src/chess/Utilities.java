package chess;

public class Utilities {
  public static int toSquarePosition(int rank, int file) {
    return rank * 8 + file;
  }

  public static String getRankAndFile(int square) {
    return String.format("%c%d", getPositionFile(square), getPositionRank(square));
  }

  public static int getPositionRank(int square) {
    return 8 - (square / 8);
  }

  public static char getPositionFile(int square) {
    return (char) ('a' + (square % 8));
  }
}
