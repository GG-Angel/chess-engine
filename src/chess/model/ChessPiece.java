package chess.model;

import java.util.Objects;

public abstract class ChessPiece implements Piece {
  private static int nextId = 0;
  private final int id;

  private final PieceColor color;
  private final PieceType type;
  private int x, y;

  public ChessPiece(PieceColor color, PieceType type, int x, int y) {
    this.id = nextId++;
    this.color = color;
    this.type = type;
    this.x = x;
    this.y = y;
  }

  @Override
  public PieceColor getColor() {
    return color;
  }

  @Override
  public PieceType getType() {
    return type;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return PieceLookup.get(this.color, this.type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Piece other = (Piece) obj;
    return this.x == other.getX() && this.y == other.getY() && this.color == other.getColor() && this.type == other.getType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
