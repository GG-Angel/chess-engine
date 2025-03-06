package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.ChessMoveType;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

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
    board.validateBounds(this.row, this.col);

    List<Move> moves = new ArrayList<>();
    moves.addAll(computeForwardMoves(board));
    moves.addAll(computeDiagonalCaptures(board));
    moves.addAll(computeEnPassant(board));
    return moves;
  }

  private List<Move> computeForwardMoves(Board board) {
    List<Move> moves = new ArrayList<>();

    for (int distance = 1; distance <= 2; distance++) {
      int toRow = this.row + (direction * distance);
      if (board.isOutOfBounds(toRow, this.col) || (board.getPieceAt(toRow, this.col) != null)) {
        return moves;
      }

      if (distance == 1 || (distance == 2 && this.row == homeRow)) {
        Move move = new ChessMove(this.row, this.col, this, toRow, this.col, null);
        if (toRow == promotionRow) {
          moves.addAll(computePromotions(move, board));
        } else {
          moves.add(move);
        }
      }
    }

    return moves;
  }

  private List<Move> computeDiagonalCaptures(Board board) {
    List<Move> moves = new ArrayList<>();
    int toRow = this.row + direction;
    int[] diagonalCols = new int[] { this.col - 1, this.col + 1 };

    for (int toCol : diagonalCols) {
      if (board.isOutOfBounds(toRow, toCol)) continue;
      Piece destPiece = board.getPieceAt(toRow, toCol);
      Move move = new ChessMove(this.row, this.col, this, toRow, toCol, destPiece);
      if (isOpposingPiece(destPiece)) {
        if (toRow == promotionRow) {
          moves.addAll(computePromotions(move, board));
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
      // check that the last move was by an opposing pawn
      // that stepped twice and landed on an adjacent slot
      Move lastMove = board.getMoveStack().peek();
      if (lastMove.fromPiece().getType() == PAWN && isOpposingPiece(lastMove.fromPiece()) &&
          (lastMove.toCol() == this.col - 1 || lastMove.toCol() == this.col + 1) &&
          lastMove.toRow() == this.row && Math.abs(lastMove.toRow() - lastMove.fromRow()) == 2
      ) {
        int toRow = this.row + direction;
        int toCol = lastMove.toCol();
        Move forwardMove = new ChessMove(this.row, toCol, this, toRow, toCol, null);
        Move captureMove = new ChessMove(this.row, this.col, this, this.row, toCol, lastMove.fromPiece(), forwardMove, ChessMoveType.EN_PASSANT);
        moves.add(captureMove);
      }
    }

    return moves;
  }

  private List<Move> computePromotions(Move originalMove, Board board) {
    List<Move> promotionMoves = new ArrayList<>();
    PieceType[] promotionTypes = new PieceType[] { KNIGHT, BISHOP, ROOK, QUEEN };
    for (PieceType type : promotionTypes) {
      Piece promotionPiece = createPiece(this.color, type, promotionRow, originalMove.toCol());
      Move promotionMove = new ChessMove(promotionRow, originalMove.toCol(), promotionPiece, promotionRow, originalMove.toCol(), this);
      promotionMoves.add(new ChessMove(
          originalMove.fromRow(), originalMove.fromCol(), this,
          promotionRow, originalMove.toCol(), originalMove.toPiece(),
          promotionMove, PROMOTION
      ));
    }
    return promotionMoves;
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♙" : "♟";
  }
}
