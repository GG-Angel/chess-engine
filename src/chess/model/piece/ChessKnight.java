package chess.model.piece;

import chess.model.Board;
import chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class ChessKnight extends ChessPiece {

  public ChessKnight(PieceColor color, int position) {
    super(color, PieceType.KNIGHT, position);
  }

  @Override
  public List<Move> calculatePseudoLegalMoves(Board board) {
    return new ArrayList<>();
  }
}
