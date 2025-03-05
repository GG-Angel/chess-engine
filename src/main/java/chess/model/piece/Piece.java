package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.Move;

import java.util.List;

public interface Piece {

  boolean hasMoved();

  boolean isOpposingPiece(Piece other);

  PieceColor getColor();

  PieceType getType();

  List<Move> getValidMoves();

  void setValidMoves(List<Move> newValidMoves);

  void setHasMoved(boolean hasMoved);

  List<Move> computeMoves(int row, int col, Board board);
}
