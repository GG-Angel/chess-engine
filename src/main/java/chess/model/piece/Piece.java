package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import java.util.ArrayList;

public interface Piece {
  PieceColor getColor();

  PieceType getType();

  ArrayList<ChessMove> getValidMoves();

  void computeValidMoves(int row, int col, ChessBoard board);
}
