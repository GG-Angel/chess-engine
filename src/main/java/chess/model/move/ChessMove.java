package chess.model.move;

public class ChessMove implements Move {
  private final int fromRow, fromCol, toRow, toCol;

  public ChessMove(int fromRow, int fromCol, int toRow, int toCol) {
    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
  }

  @Override
  public int fromRow() {
    return this.fromRow;
  }

  @Override
  public int fromCol() {
    return this.fromCol;
  }

  @Override
  public int toRow() {
    return this.toRow;
  }

  @Override
  public int toCol() {
    return this.toCol;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d) → (%d, %d)", fromRow, fromCol, toRow, toCol);
  }
}
