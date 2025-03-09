package chess.model.piece;

import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class ChessPiece implements Piece {
  private static int nextId = 0;
  private final int id;

  protected int row, col;
  protected PieceColor color;
  protected PieceType type;
  protected List<Move> validMoves;
  protected boolean isAlive, hasMovedBefore;

  protected ChessPiece(PieceColor color, PieceType type, int row, int col) throws NullPointerException {
    this.color = requireNonNull(color, "Must pass non-null Color to Piece.");
    this.type = requireNonNull(type, "Must pass non-null Type to Piece.");
    this.id = nextId++;
    this.row = row;
    this.col = col;
    this.validMoves = new ArrayList<>();
    this.isAlive = true;
    this.hasMovedBefore = false;
  }

  protected boolean isOpposingPiece(Piece other) {
    return other != null && this.color != other.getColor();
  }

  @Override
  public boolean isAlive() {
    return this.isAlive;
  }

  @Override
  public boolean hasMovedBefore() {
    return this.hasMovedBefore;
  }

  @Override
  public PieceColor getColor() {
    return this.color;
  }

  @Override
  public PieceType getType() {
    return this.type;
  }

  @Override
  public List<Move> getValidMoves() {
    return this.validMoves;
  }

  @Override
  public void setValidMoves(List<Move> newValidMoves) {
    this.validMoves = newValidMoves;
  }

  @Override
  public void setIsAlive(boolean isAlive) {
    this.isAlive = isAlive;
  }

  @Override
  public void setHasMovedBefore(boolean hasMovedBefore) {
    this.hasMovedBefore = hasMovedBefore;
  }

  @Override
  public void setPosition(int row, int col) {
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    ChessPiece that = (ChessPiece) obj;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
