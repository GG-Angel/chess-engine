package chess;

public enum Color {
  WHITE (0), // 0
  BLACK (6); // 1

  private final int startIndex;

  Color(int startIndex) {
    this.startIndex = startIndex;
  }

  public int startIndex() {
    return startIndex;
  }
}
