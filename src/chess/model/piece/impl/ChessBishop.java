package chess.model.piece.impl;

import chess.model.board.Board;
import chess.model.move.Move;

import chess.model.piece.abstracts.DirectionalPiece;
import chess.model.piece.abstracts.PieceColor;
import chess.model.piece.abstracts.PieceType;
import java.util.List;

public class ChessBishop extends DirectionalPiece {

  public ChessBishop(PieceColor color, int row, int col) {
    super(color, PieceType.BISHOP, row, col);
  }

  @Override
  public List<Move> computeMoves(Board board) {
    int[][] directions = new int[][] { {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };
    return computeMoves(directions, board);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♗" : "♝";
  }
}
