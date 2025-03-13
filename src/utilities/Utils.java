package utilities;

public class Utils {
  public static int to1D(int x, int y) {
    return y * 8 + x;
  }

  public static int[] to2D(int position) {
    int x = position % 8;
    int y = position / 8;
    return new int[] { x, y };
  }

  public static String getRankAndFile(int position) {
    return String.format("%c%d", getPositionFile(position), getPositionRank(position));
  }

  public static int getPositionRank(int position) {
    return 8 - (position / 8);
  }

  public static char getPositionFile(int position) {
    return (char) ('a' + (position % 8));
  }
}
