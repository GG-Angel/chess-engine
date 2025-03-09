package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.ChessMoveType;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public class ChessKing extends ProximityPiece {
  private final int homeRow;

  public ChessKing(PieceColor color, int row, int col) {
    super(color, PieceType.KING, row, col);
    this.homeRow = this.color == PieceColor.WHITE ? 7 : 0;
  }

  @Override
  public List<Move> computeMoves(Board board) throws IndexOutOfBoundsException {
    int[][] distances = new int[][] {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
    List<Move> moves = computeMoves(distances, board);
    List<Move> castlingMoves = computeCastling(board);

    moves.addAll(castlingMoves);
    return moves;
  }

  private List<Move> computeCastling(Board board) {
    List<Move> moves = new ArrayList<>();
    if (hasMovedBefore || this.row != homeRow || board.isKingInCheck()) return moves;

    checkCastling(board, 7, 6, 5, moves); // king-side castling
    checkCastling(board, 0, 2, 3, moves); // queen-side castling

    return moves;
  }

  private void checkCastling(Board board, int rookCol, int kingTargetCol, int rookTargetCol, List<Move> moves) {
    Piece rook = board.getPieceAt(homeRow, rookCol);

    // ensure we are castling with a friendly rook
    if (rook == null || rook.getType() != PieceType.ROOK || isOpposingPiece(rook) || rook.hasMovedBefore()) return;

    // check that the castling path is uninterrupted
    int step = (rookCol == 7) ? 1 : -1;
    for (int col = this.col + step; col != rookCol; col += step) {
      if (board.getPieceAt(homeRow, col) != null) return;
    }

    // check that the castling path is not being attacked
    PieceColor opponentColor = board.getOpposingColor(this.color);
    List<Move> opponentMoves = board.getMoves().get(opponentColor);
    for (Move opponentMove : opponentMoves) {
      if (opponentMove.toRow() == homeRow) {
        for (int col = this.col + step; col != this.col + (step * 3); col += step) {
          if (opponentMove.toCol() == col) {
            return;
          }
        }
      }
    }

    Move rookMove = new ChessMove(this.row, rookCol, rook, this.row, rookTargetCol, null, ChessMoveType.CASTLE);
    Move kingMove = new ChessMove(this.row, this.col, this, this.row, kingTargetCol, null, rookMove, ChessMoveType.CASTLE);
    moves.add(kingMove);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
