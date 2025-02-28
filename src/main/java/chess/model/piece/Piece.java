package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.move.Move;
import java.util.ArrayList;

public interface Piece {
  PieceColor getColor();

  PieceType getType();

  ArrayList<Move> getValidMoves();

  void setHasMoved(boolean hasMoved);

  void computeValidMoves(int row, int col, ChessBoard board);
}
