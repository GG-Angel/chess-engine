package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.move.Move;
import java.lang.reflect.Array;
import java.util.ArrayList;

public interface Piece {

  boolean hasMoved();

  boolean isOpposingPiece(Piece other);

  PieceColor getColor();

  PieceType getType();

  ArrayList<Move> getPossibleMoves();

  ArrayList<Move> getValidMoves();

  void clearMoves();

  void setHasMoved(boolean hasMoved);

  void computeMoves(int row, int col, ChessBoard board);
}
