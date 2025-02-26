package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.Move;
import java.util.ArrayList;

public interface IPiece {
  PieceColor getColor();

  PieceType getType();

  ArrayList<Move> getValidMoves();

  void computeValidMoves(int row, int col, ChessBoard board);
}
