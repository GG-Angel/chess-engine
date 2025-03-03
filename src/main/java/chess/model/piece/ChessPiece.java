package chess.model.piece;

import static java.util.Objects.requireNonNull;

import chess.model.board.ChessBoard;
import chess.model.move.Move;
import java.util.ArrayList;
import java.util.Objects;

public abstract class ChessPiece implements Piece {
  public static ChessPiece createPiece(PieceColor color, PieceType type) throws NullPointerException {
    return switch (type) {
      case PAWN -> new ChessPawn(color);
      case BISHOP -> new ChessBishop(color);
      case KNIGHT -> new ChessKnight(color);
      case ROOK -> new ChessRook(color);
      case QUEEN -> new ChessQueen(color);
      case KING -> new ChessKing(color);
    };
  }

  protected PieceColor color;
  protected PieceType type;
  protected ArrayList<Move> possibleMoves;
  protected ArrayList<Move> validMoves;
  protected boolean hasMoved;

  protected ChessPiece(PieceColor color, PieceType type) throws NullPointerException {
    this.color = requireNonNull(color, "Must pass non-null Color to Piece.");
    this.type = requireNonNull(type, "Must pass non-null Type to Piece.");
    this.validMoves = new ArrayList<>();
    this.hasMoved = false;
  }

  @Override
  public boolean hasMoved() {
    return this.hasMoved;
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
  public ArrayList<Move> getPossibleMoves() {
    return this.possibleMoves;
  }

  @Override
  public ArrayList<Move> getValidMoves() {
    return this.validMoves;
  }

  @Override
  public void clearMoves() {
    possibleMoves = new ArrayList<>();
    validMoves = new ArrayList<>();
  }

  @Override
  public void setHasMoved(boolean hasMoved) {
    this.hasMoved = hasMoved;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    ChessPiece that = (ChessPiece) obj;
    return color == that.color && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, type);
  }
}
