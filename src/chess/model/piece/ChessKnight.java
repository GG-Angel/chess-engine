package chess.model.piece;

import static chess.model.piece.PieceConstants.NUM_SQUARES_FROM_EDGE;

import chess.model.Board;
import chess.model.ChessMove;
import chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class ChessKnight extends ChessPiece {
  // directions are ordered from left to right of knight
  private static final int[] directions = new int[] { -10, 6, -17, 15, -15, 17, -6, 10 };

  public ChessKnight(PieceColor color, int position) {
    super(color, PieceType.KNIGHT, position);
  }

  @Override
  public List<Move> calculatePseudoLegalMoves(Board board) {
    List<Move> moves = new ArrayList<>();
    List<Integer> attacking = new ArrayList<>();

    int edgeLeftDistance = NUM_SQUARES_FROM_EDGE[this.position][1];
    int edgeRightDistance = NUM_SQUARES_FROM_EDGE[this.position][2];

    int startDirIndex = edgeLeftDistance >= 2 ? 0 : edgeLeftDistance == 1 ? 2 : 0;
    int endDirIndex = edgeRightDistance >= 2 ? 8 : edgeRightDistance == 1 ? 6 : 4;

    for (int dirIndex = startDirIndex; dirIndex < endDirIndex; dirIndex++) {
      int targetPosition = this.position + directions[dirIndex];

      if (targetPosition < 0 || targetPosition >= 64) {
        continue;
      }

      attacking.add(targetPosition);

      Piece targetPiece = board.getPieceAtPosition(targetPosition);
      if (isEmpty(targetPiece) || !isColor(targetPiece, this.color)) {
        moves.add(new ChessMove(this.position, targetPosition, this));
      }
    }

    this.pseudoLegalMoves = moves;
    this.attackingPositions = attacking;
    return moves;
  }
}
