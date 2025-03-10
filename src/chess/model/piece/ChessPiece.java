package chess.model.piece;

import java.util.Objects;

public abstract class ChessPiece implements Piece {
  private static int nextId = 0;
  private final int id;

  private final PieceColor color;
  private final PieceType type;
  private int position;

  public ChessPiece(PieceColor color, PieceType type, int position) {
    this.id = nextId++;
    this.color = color;
    this.type = type;
    this.position = position;
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
  public int getPosition() {
    return position;
  }

  @Override
  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return PieceLookup.getSymbol(this.color, this.type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Piece other = (Piece) obj;
    return
        this.position == other.getPosition() &&
        this.color == other.getColor() &&
        this.type == other.getType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
