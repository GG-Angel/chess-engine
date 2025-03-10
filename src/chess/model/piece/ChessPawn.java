package chess.model.piece;

import chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class ChessPawn extends ChessPiece {

  public ChessPawn(PieceColor color, int position) {
    super(color, PieceType.PAWN, position);
  }

  @Override
  public List<Move> calculatePseudoLegalMoves() {
    return new ArrayList<>();
  }
}
