package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.ChessMoveType;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

import static chess.model.piece.PieceType.*;

public class ChessPawn extends ChessPiece {

  private final int direction;

  public ChessPawn(PieceColor color) {
    super(color, PieceType.PAWN);
    this.direction = this.color == PieceColor.WHITE ? -1 : 1;
  }

  @Override
  public List<Move> computeMoves(int fromRow, int fromCol, Board board) {
    board.validateBounds(fromRow, fromCol);
    List<Move> moves = new ArrayList<>();

    computeForwardMovesAndPromotions(fromRow, fromCol, board, moves);
    computeDiagonalCaptures(fromRow, fromCol, board, moves);
    computeEnPassant(fromRow, fromCol, board, moves);

    return moves;
  }

  private void computeForwardMovesAndPromotions(int fromRow, int fromCol, Board board, List<Move> moves) {
    int homeRow = this.color == PieceColor.WHITE ? 6 : 1;
    int promotionRow = this.color == PieceColor.WHITE ? 0 : 7;
    for (int distance = 1; distance <= 2; distance++) {
      int toRow = fromRow + (direction * distance);
      if (board.isOutOfBounds(toRow, fromCol) || (board.getPieceAt(toRow, fromCol) != null)) return;
      if (distance == 1 || (distance == 2 && fromRow == homeRow)) {
        if (toRow == promotionRow) {
          PieceType[] promotionTypes = new PieceType[] { KNIGHT, BISHOP, ROOK, QUEEN };
          for (PieceType type : promotionTypes) {
            Piece promotionPiece = createPiece(this.color, type);
            Move promotionMove = new ChessMove(fromRow, fromCol, this, toRow, fromCol, promotionPiece);
            Move forwardMove = new ChessMove(fromRow, fromCol, this, toRow, fromCol, null, promotionMove, ChessMoveType.PROMOTION);
            moves.add(forwardMove);
          }
        } else {
          Move move = new ChessMove(fromRow, fromCol, this, toRow, fromCol, null);
          moves.add(move);
        }
      }
    }
  }

  private void computeDiagonalCaptures(int fromRow, int fromCol, Board board, List<Move> moves) {
    int toRow = fromRow + direction;
    int[] diagonalCols = new int[] { fromCol - 1, fromCol + 1 };
    for (int toCol : diagonalCols) {
      if (board.isOutOfBounds(toRow, toCol)) continue;
      Piece destPiece = board.getPieceAt(toRow, toCol);
      Move move = new ChessMove(fromRow, fromCol, this, toRow, toCol, destPiece);
      if (isOpposingPiece(destPiece)) {
        moves.add(move);
      }
    }
  }

  private void computeEnPassant(int fromRow, int fromCol, Board board, List<Move> moves) {
    int toRow = fromRow + direction;
    if (board.isOutOfBounds(toRow, fromCol)) return; // ensure there is an extra row forward

    if (board.getMoveStack().isEmpty()) return;
    Move lastMove = board.getMoveStack().peek();

    // check that the last move was by an opposing pawn
    // that stepped twice and landed on an adjacent slot
    if (lastMove.fromPiece().getType() != PieceType.PAWN || !isOpposingPiece(lastMove.fromPiece())) return;
    if (lastMove.toRow() != fromRow) return;
    if (Math.abs(lastMove.toRow() - lastMove.fromRow()) != 2) return;
    if (lastMove.toCol() == fromCol - 1 || lastMove.toCol() == fromCol + 1) {
      int toCol = lastMove.toCol();
      Move forwardMove = new ChessMove(fromRow, toCol, this, toRow, toCol, null);
      Move captureMove = new ChessMove(fromRow, fromCol, this, fromRow, toCol, lastMove.fromPiece(), forwardMove, ChessMoveType.EN_PASSANT);
      moves.add(captureMove);
    }
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♙" : "♟";
  }
}
