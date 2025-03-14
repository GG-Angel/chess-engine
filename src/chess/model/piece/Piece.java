package chess.model.piece;

import chess.model.Board;
import chess.model.Move;
import java.util.List;

public interface Piece {

  List<Move> calculatePseudoLegalMoves(Board board);

  List<Move> getPseudoLegalMoves();

  List<Integer> getPositionsControlled();

  PieceColor getColor();

  PieceType getType();

  int getPosition();

  void setPosition(int position);
}
