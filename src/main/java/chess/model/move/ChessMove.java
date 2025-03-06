package chess.model.move;

import chess.model.piece.abstracts.Piece;
import chess.model.piece.abstracts.PieceType;

import chess.model.piece.impl.ChessPawn;
import java.util.Objects;

import static chess.model.move.ChessMoveType.STANDARD;
import static java.util.Objects.requireNonNull;

public class ChessMove implements Move, Comparable<ChessMove> {
  private int fromRow, fromCol, toRow, toCol;
  private Piece fromPiece, toPiece;
  private Move subMove;
  private ChessMoveType moveType;
  private boolean wasFirstMove;

  public ChessMove(int fromRow, int fromCol, Piece fromPiece, int toRow, int toCol, Piece toPiece) {
    initializeMove(fromRow, fromCol, fromPiece, toRow, toCol, toPiece, null, STANDARD);
  }

  public ChessMove(int fromRow, int fromCol, Piece fromPiece, int toRow, int toCol, Piece toPiece, Move subMove, ChessMoveType moveType) {
    initializeMove(fromRow, fromCol, fromPiece, toRow, toCol, toPiece, subMove, moveType);
  }

  public ChessMove(int fromRow, int fromCol, ChessPawn fromPiece, int toRow, int toCol, Piece promotionPiece, ChessMoveType chessMoveType) {
    initializeMove(fromRow, fromCol, fromPiece, toRow, toCol, promotionPiece, null, chessMoveType);
  }

  private void initializeMove(int fromRow, int fromCol, Piece fromPiece, int toRow, int toCol, Piece toPiece, Move subMove, ChessMoveType moveType) throws NullPointerException {
      this.fromPiece = requireNonNull(fromPiece, "No piece to move specified.");
      this.fromRow = fromRow;
      this.fromCol = fromCol;
      this.wasFirstMove = !fromPiece.hasMoved();
      this.toRow = toRow;
      this.toCol = toCol;
      this.toPiece = toPiece;
      this.subMove = subMove;
      this.moveType = moveType;
    }

  @Override
  public boolean collidesWith(Move other) {
    return this.toRow == other.toRow() && this.toCol == other.toCol();
  }

  @Override
  public boolean threatensKing() {
    return this.toPiece != null && this.toPiece.getType() == PieceType.KING;
  }

  @Override
  public boolean wasFirstMove() {
    return this.wasFirstMove;
  }

  @Override
  public Move getSubMove() {
    return this.subMove;
  }

  @Override
  public ChessMoveType getMoveType() {
    return this.moveType;
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
  public Piece fromPiece() {
    return this.fromPiece;
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
  public Piece toPiece() {
    return this.toPiece;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(moveType).append(": ");
    return recToString(sb, this);
  }

  private String recToString(StringBuilder sb, Move move) {
    String to = toPiece != null ? toPiece.toString() : "_";
    sb.append(String.format("(%d, %d, %s) → (%d, %d, %s)",
            fromRow, fromCol, fromPiece, toRow, toCol, to));
    if (move.getSubMove() != null) {
      sb.append(" ==> ");
      return recToString(sb, getSubMove());
    } else {
      return sb.toString();
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    ChessMove chessMove = (ChessMove) obj;
    return fromRow == chessMove.fromRow &&
           fromCol == chessMove.fromCol &&
           toRow == chessMove.toRow &&
           toCol == chessMove.toCol;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromRow, fromCol, toRow, toCol);
  }

  @Override
  public int compareTo(ChessMove other) {
    if (this.fromRow != other.fromRow) {
      return Integer.compare(this.fromRow, other.fromRow);
    }
    if (this.fromCol != other.fromCol) {
      return Integer.compare(this.fromCol, other.fromCol);
    }
    if (this.toRow != other.toRow) {
      return Integer.compare(this.toRow, other.toRow);
    }
    return Integer.compare(this.toCol, other.toCol);
  }
}
