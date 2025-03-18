package chess;

public record Move(int from, int to, byte flags) {

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
