package chess.model.piece;

import chess.model.Move;
import java.util.List;

public interface Piece {

  List<Move> calculatePseudoLegalMoves();

  List<Integer> getAttackingPositions();

  PieceColor getColor();

  PieceType getType();

  int getPosition();

  void setPosition(int position);
}
