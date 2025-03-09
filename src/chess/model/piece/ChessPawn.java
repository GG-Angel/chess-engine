package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.model.move.ChessMoveType.DOUBLE_STEP;
import static chess.model.move.ChessMoveType.EN_PASSANT;
import static chess.model.move.ChessMoveType.PROMOTION;
import static chess.model.piece.PieceType.*;

public class ChessPawn extends ChessPiece {
  private final int direction, homeRow, promotionRow;

  public ChessPawn(PieceColor color, int row, int col) {
    super(color, PieceType.PAWN, row, col);
    this.direction = this.color == PieceColor.WHITE ? -1 : 1;
    this.homeRow = this.color == PieceColor.WHITE ? 6 : 1;
    this.promotionRow = this.color == PieceColor.WHITE ? 0 : 7;
  }

  @Override
  public List<Move> computeMoves(Board board) {
    List<Move> moves = new ArrayList<>();
    moves.addAll(computeForwardMoves(board));
    moves.addAll(computeDiagonalCaptures(board));
    moves.addAll(computeEnPassant(board));
    return moves;
  }

  private List<Move> computeForwardMoves(Board board) {
    List<Move> moves = new ArrayList<>();

    for (int distance = 1; distance <= 2; distance++) {
      if (distance == 2 && this.row != homeRow) break;

      int toRow = this.row + (direction * distance);
      if (board.isOutOfBounds(toRow, this.col) || (board.getPieceAt(toRow, this.col) != null)) break;

      if (distance == 1) {
        Move move = new ChessMove(this.row, this.col, this, toRow, this.col, null);
        if (toRow == promotionRow) {
          moves.addAll(computePromotions(move));
        } else {
          moves.add(move);
        }
      } else {
        moves.add(new ChessMove(this.row, this.col, this, toRow, this.col, null, DOUBLE_STEP));
      }
    }

    return moves;
  }

  private List<Move> computeDiagonalCaptures(Board board) {
    List<Move> moves = new ArrayList<>();
    int toRow = this.row + direction;
    for (int toCol = this.col - 1; toCol <= this.col + 1; toCol += 2) {
      if (board.isOutOfBounds(toRow, toCol)) continue;
      Piece toPiece = board.getPieceAt(toRow, toCol);
      if (isOpposingPiece(toPiece)) {
        Move move = new ChessMove(this.row, this.col, this, toRow, toCol, toPiece);
        if (toRow == promotionRow) {
          moves.addAll(computePromotions(move));
        } else {
          moves.add(move);
        }
      }
    }

    return moves;
  }

  private List<Move> computeEnPassant(Board board) {
    List<Move> moves = new ArrayList<>();
    if (!board.getMoveStack().isEmpty()) {
      Move lastMove = board.getMoveStack().peek();
      boolean isPawnAdjacent = lastMove.toRow() == this.row && (lastMove.toCol() == this.col - 1 || lastMove.toCol() == this.col + 1);
      if (lastMove.getMoveType() == DOUBLE_STEP && isPawnAdjacent) {
        int toRow = this.row + direction;
        int toCol = lastMove.toCol();
        Move forwardMove = new ChessMove(this.row, toCol, this, toRow, toCol, null, EN_PASSANT);
        Move captureMove = new ChessMove(this.row, this.col, this, this.row, toCol, lastMove.fromPiece(), EN_PASSANT, forwardMove);
        moves.add(captureMove);
      }
    }
    return moves;
  }

  private List<Move> computePromotions(Move originalMove) {
    Piece capturedPiece = originalMove.toPiece();
    int fromRow = originalMove.fromRow();
    int fromCol = originalMove.fromCol();
    int toCol = originalMove.toCol();

    List<Move> promotionMoves = new ArrayList<>();
    PieceType[] promotionTypes = new PieceType[] { KNIGHT, BISHOP, ROOK, QUEEN };

    for (PieceType type : promotionTypes) {
      Piece promotionPiece = PieceFactory.createPiece(this.color, type, promotionRow, toCol);
      Move promotionMove = new ChessMove(promotionRow, toCol, promotionPiece, promotionRow, toCol, this, PROMOTION);
      Move originalWithPromotion = new ChessMove(fromRow, fromCol, this, promotionRow, toCol, capturedPiece, PROMOTION, promotionMove);
      promotionMoves.add(originalWithPromotion);
    }
    return promotionMoves;
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♙" : "♟";
  }
}
