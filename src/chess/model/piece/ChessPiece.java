package chess.model.piece;

import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class ChessPiece implements Piece {
  public static ChessPiece createPiece(PieceColor color, PieceType type, int row, int col) throws NullPointerException {
    return switch (type) {
      case PAWN -> new ChessPawn(color, row, col);
      case BISHOP -> new ChessBishop(color, row, col);
      case KNIGHT -> new ChessKnight(color, row, col);
      case ROOK -> new ChessRook(color, row, col);
      case QUEEN -> new ChessQueen(color, row, col);
      case KING -> new ChessKing(color, row, col);
    };
  }

  protected int row, col;
  protected PieceColor color;
  protected PieceType type;
  protected List<Move> validMoves;
  protected boolean isAlive, hasMovedBefore;

  protected ChessPiece(PieceColor color, PieceType type, int row, int col) throws NullPointerException {
    this.color = requireNonNull(color, "Must pass non-null Color to Piece.");
    this.type = requireNonNull(type, "Must pass non-null Type to Piece.");
    this.row = row;
    this.col = col;
    this.validMoves = new ArrayList<>();
    this.isAlive = true;
    this.hasMovedBefore = false;
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
  public boolean isOpposingPiece(Piece other) {
    return other != null && this.color != other.getColor();
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
    return row == that.row && col == that.col && color == that.color && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col, color, type);
  }
}
