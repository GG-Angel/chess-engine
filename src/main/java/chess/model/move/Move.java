package chess.model.move;

public class Move {
  private final int row;
  private final int col;

  public Move(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", row, col);
  }
}
