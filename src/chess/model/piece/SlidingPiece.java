package chess.model.piece;

import static chess.model.ChessBoard.BOARD_SIZE;

import chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class SlidingPiece extends ChessPiece {
  public SlidingPiece(PieceColor color, PieceType type, int position) {
    super(color, type, position);
  }

  @Override
  public List<Move> calculatePseudoLegalMoves() {
    int startDirIndex = isType(this, PieceType.BISHOP) ? 4 : 0;
    int endDirIndex = isType(this, PieceType.ROOK) ? 4 : 8;

    for (int dirIndex = startDirIndex; dirIndex < endDirIndex; dirIndex++) {
      for (int dist = 0; dist < BOARD_SIZE; dist++) {

      }
    }

    return new ArrayList<>();
  }
}
