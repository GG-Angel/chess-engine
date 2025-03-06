package chess.model.piece.impl;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.ChessMoveType;
import chess.model.move.Move;

import chess.model.piece.abstracts.Piece;
import chess.model.piece.abstracts.PieceColor;
import chess.model.piece.abstracts.PieceType;
import chess.model.piece.abstracts.ProximityPiece;
import java.util.ArrayList;
import java.util.List;

public class ChessKing extends ProximityPiece {

  public ChessKing(PieceColor color, int row, int col) {
    super(color, PieceType.KING, row, col);
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
    if (hasMoved || board.isCurrentKingInCheck()) return moves;

    checkCastling(board, 7, 6, 5, moves); // king-side castling
    checkCastling(board, 0, 2, 3, moves); // queen-side castling

    return moves;
  }

  private void checkCastling(Board board, int rookCol, int kingTargetCol, int rookTargetCol, List<Move> moves) {
    Piece rook = board.getPieceAt(this.row, rookCol);
    if (rook == null || rook.getType() != PieceType.ROOK || isOpposingPiece(rook) || rook.hasMoved()) return;

    int step = (rookCol == 7) ? 1 : -1;
    for (int col = this.col + step; col != rookCol; col += step) {
      if (board.getPieceAt(this.row, col) != null) return; // collision detected
    }

    Move rookMove = new ChessMove(this.row, rookCol, rook, this.row, rookTargetCol, null);
    Move kingMove = new ChessMove(this.row, this.col, this, this.row, kingTargetCol, null, rookMove, ChessMoveType.CASTLE);
    moves.add(kingMove);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
