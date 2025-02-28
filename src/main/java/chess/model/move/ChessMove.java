package chess.model.move;

import static java.util.Objects.requireNonNull;

import chess.model.board.ChessBoard;
import chess.model.piece.Piece;
import java.util.Objects;

public class ChessMove implements Move, Comparable<ChessMove> {
  private int fromRow, fromCol, toRow, toCol;
  private Piece fromPiece, toPiece;

  public ChessMove(int fromRow, int fromCol, int toRow, int toCol, ChessBoard board) throws IndexOutOfBoundsException, NullPointerException {
    if (!board.isInBounds(fromRow, fromCol) || !board.isInBounds(toRow, toCol)) {
      throw new IndexOutOfBoundsException("Move coordinates are out of bounds.");
    }

    Piece fromPiece = board.getPieceAt(fromRow, fromCol);
    Piece toPiece = board.getPieceAt(toRow, toCol);
    initializeMove(fromRow, fromCol, fromPiece, toRow, toCol, toPiece);
  }

  public ChessMove(int fromRow, int fromCol, Piece fromPiece, int toRow, int toCol, Piece toPiece) throws NullPointerException {
    initializeMove(fromRow, fromCol, fromPiece, toRow, toCol, toPiece);
  }

  private void initializeMove(int fromRow, int fromCol, Piece fromPiece, int toRow, int toCol, Piece toPiece) throws NullPointerException {
    this.fromPiece = requireNonNull(fromPiece, "No piece to move specified.");
    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.toPiece = toPiece;
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
    String to = toPiece != null ? toPiece.toString() : "_";
    return String.format("(%d, %d, %s) → (%d, %d, %s)", fromRow, fromCol, fromPiece, toRow, toCol, to);
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
