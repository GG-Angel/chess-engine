package chess.model.piece;

import chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class ChessKing extends ChessPiece {

  public ChessKing(PieceColor color, int position) {
    super(color, PieceType.KING, position);
  }

  @Override
  public List<Move> calculatePseudoLegalMoves() {
    return new ArrayList<>();
  }
}
