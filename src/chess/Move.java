package chess;

public class Move {
  private final int from, to;
  private final byte flags;

  public Move(int from, int to, byte flags) {
    this.from = from;
    this.to = to;
    this.flags = flags;
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }

  public byte getFlags() {
    return flags;
  }



  @Override
  public String toString() {
    return squareToString(from) + squareToString(to);
  }

  private String squareToString(int square) {
    char file = (char) ('a' + (square % 8));
    int rank = square / 8 + 1;
    return "" + file + rank;
  }
}
