package chess.model.piece.impl;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.ChessMoveType;
import chess.model.move.Move;

import chess.model.piece.abstracts.ChessPiece;
import chess.model.piece.abstracts.Piece;
import chess.model.piece.abstracts.PieceColor;
import chess.model.piece.abstracts.PieceType;
import java.util.ArrayList;
import java.util.List;

import static chess.model.piece.abstracts.PieceType.*;

public class ChessPawn extends ChessPiece {

  private final int direction;

  public ChessPawn(PieceColor color, int row, int col) {
    super(color, PieceType.PAWN, row, col);
    this.direction = this.color == PieceColor.WHITE ? -1 : 1;
  }

  @Override
  public List<Move> computeMoves(Board board) {
    board.validateBounds(this.row, this.col);

    List<Move> moves = new ArrayList<>();
    moves.addAll(computeForwardMovesAndPromotions(board));
    moves.addAll(computeDiagonalCaptures(board));
    moves.addAll(computeEnPassant(board));
    return moves;
  }

  private List<Move> computeForwardMovesAndPromotions(Board board) {
    List<Move> moves = new ArrayList<>();
    int homeRow = this.color == PieceColor.WHITE ? 6 : 1;
    int promotionRow = this.color == PieceColor.WHITE ? 0 : 7;

    for (int distance = 1; distance <= 2; distance++) {
      int toRow = this.row + (direction * distance);
      if (board.isOutOfBounds(toRow, this.col) || (board.getPieceAt(toRow, this.col) != null)) {
        return moves;
      }

      if (distance == 1 || (distance == 2 && this.row == homeRow)) {
        if (toRow == promotionRow) {
          PieceType[] promotionTypes = new PieceType[] { KNIGHT, BISHOP, ROOK, QUEEN };
          for (PieceType type : promotionTypes) {
            Piece promotionPiece = createPiece(this.color, type, toRow, this.col);
            Move promotionMove = new ChessMove(this.row, this.col, this, toRow, this.col, promotionPiece, ChessMoveType.PROMOTION);
            moves.add(promotionMove);
          }
        } else {
          Move move = new ChessMove(this.row, this.col, this, toRow, this.col, null);
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
        moves.add(move);
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

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♙" : "♟";
  }
}
