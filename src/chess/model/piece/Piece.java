package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.Move;

import java.util.List;

public interface Piece {

  boolean isAlive();

  boolean hasMovedBefore();

  PieceColor getColor();

  PieceType getType();

  List<Move> computeMoves(Board board);

  List<Move> getValidMoves();

  void setValidMoves(List<Move> newValidMoves);

  void setHasMovedBefore(boolean hasMovedBefore);

  void setIsAlive(boolean isAlive);

  void setPosition(int row, int col);

}
