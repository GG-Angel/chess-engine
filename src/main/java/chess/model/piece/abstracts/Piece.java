package chess.model.piece.abstracts;

import chess.model.board.Board;
import chess.model.move.Move;

import java.util.List;

public interface Piece {

  boolean getIsAlive();

  boolean getHasMoved();

  boolean isOpposingPiece(Piece other);

  PieceColor getColor();

  PieceType getType();

  List<Move> getValidMoves();

  void setValidMoves(List<Move> newValidMoves);

  void setHasMoved(boolean hasMoved);

  void setIsAlive(boolean isAlive);

  void setPosition(int row, int col);

  List<Move> computeMoves(Board board);
}
