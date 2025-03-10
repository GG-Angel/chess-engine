package chess.model;

import static chess.model.MoveType.CASTLING;
import static chess.model.MoveType.EN_PASSANT;
import static chess.model.MoveType.NORMAL;
import static chess.model.MoveType.PROMOTION;
import static chess.model.piece.PieceColor.BLACK;
import static chess.model.piece.PieceLookup.getPieceSymbol;
import static utilities.Utils.getRankAndFile;

import chess.model.piece.Piece;
import chess.model.piece.PieceFactory;
import chess.model.piece.PieceType;
import java.util.Objects;

public class ChessMove implements Move {
  // always present
  private final int from, to;
  private final Piece piece;
  private final MoveType moveType;

  // presence is based on move type
  private final PieceType promotionPieceType;
  private final Move castlingMove;
  private final int enPassantPawnPosition;

  public ChessMove(int from, int to, Piece piece) {
    this(from, to, piece, null, null, -1, NORMAL);
  }

  public ChessMove(int from, int to, Piece piece, PieceType promotionPieceType) {
    this(from, to, piece, promotionPieceType, null, -1, PROMOTION);
  }

  public ChessMove(int from, int to, Piece piece, Move castlingMove) {
    this(from, to, piece, null, castlingMove, -1, CASTLING);
  }

  public ChessMove(int from, int to, Piece piece, int enPassantPawnPosition) {
    this(from, to, piece, null, null, enPassantPawnPosition, EN_PASSANT);
  }

  public ChessMove(int from, int to, Piece piece, PieceType promotionPieceType, Move castlingMove, int enPassantPawnPosition, MoveType movetype) {
    this.from = from;
    this.to = to;
    this.piece = piece;
    this.promotionPieceType = promotionPieceType;
    this.castlingMove = castlingMove;
    this.enPassantPawnPosition = enPassantPawnPosition;
    this.moveType = movetype;
  }

  @Override
  public int getFrom() {
    return from;
  }

  @Override
  public int getTo() {
    return to;
  }

  @Override
  public Piece getPiece() {
    return piece;
  }

  @Override
  public MoveType getMoveType() {
    return moveType;
  }

  @Override
  public Piece getPromotionPiece() throws IllegalStateException {
    if (moveType != PROMOTION) {
      throw new IllegalStateException("This is not a promotion move.");
    }
    return PieceFactory.createPiece(piece.getColor(), promotionPieceType, to);
  }

  @Override
  public Move getCastlingMove() throws IllegalStateException {
    if (moveType != CASTLING) {
      throw new IllegalStateException("This is not a promotion move.");
    }
    return castlingMove;
  }

  @Override
  public int getEnPassantPawnPosition() throws IllegalStateException {
    if (moveType != EN_PASSANT) {
      throw new IllegalStateException("This is not an en passant move.");
    }
    return enPassantPawnPosition;
  }

  @Override
  public String toString() {
    return String.format(
        "%s%s%s",
        getRankAndFile(from), getRankAndFile(to),
        moveType == PROMOTION ? getPieceSymbol(BLACK, promotionPieceType) : ""
    );
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    ChessMove that = (ChessMove) obj;

    // Basic move equality based on from, to, piece, and moveType
    if (from != that.from) return false;
    if (to != that.to) return false;
    if (!Objects.equals(piece, that.piece)) return false;
    if (moveType != that.moveType) return false;

    // Additional checks based on moveType
    if (moveType == PROMOTION) {
      return Objects.equals(promotionPieceType, that.promotionPieceType);
    } else if (moveType == CASTLING) {
      return Objects.equals(castlingMove, that.castlingMove);
    } else if (moveType == EN_PASSANT) {
      return enPassantPawnPosition == that.enPassantPawnPosition;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, piece, moveType, promotionPieceType, castlingMove, enPassantPawnPosition);
  }
}
