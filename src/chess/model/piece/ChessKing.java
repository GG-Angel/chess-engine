package chess.model.piece;

import static chess.model.piece.PieceColor.WHITE;
import static chess.model.piece.PieceConstants.DIRECTION_OFFSETS;
import static chess.model.piece.PieceConstants.NUM_SQUARES_FROM_EDGE;

import chess.model.Board;
import chess.model.ChessMove;
import chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class ChessKing extends ChessPiece {
  private final int[] castleDirections = new int[] { 2, 1, -1, -2, -3 };
  private final int castlingRow;

  public ChessKing(PieceColor color, int position) {
    super(color, PieceType.KING, position);
    this.castlingRow = isColor(this, WHITE) ? 7 : 0;
  }

  @Override
  public List<Move> calculatePseudoLegalMoves(Board board) {
    List<Move> moves = new ArrayList<>();
    List<Integer> attacking = new ArrayList<>();

    int[] distancesFromEdges = NUM_SQUARES_FROM_EDGE[this.position];

    // calculate surrounding moves
    for (int dirIndex = 0; dirIndex < 8; dirIndex++) {
      if (distancesFromEdges[dirIndex] == 0) {
        continue;
      }

      int targetPosition = this.position + DIRECTION_OFFSETS[dirIndex];
      Piece targetPiece = board.getPieceAtPosition(targetPosition);
      attacking.add(targetPosition);

      if (isEmpty(targetPiece) || !isColor(targetPiece, this.color)) {
        moves.add(new ChessMove(this.position, targetPosition, this));
      }
    }

    // TODO: Calculate castling moves
    // TODO: Verify king and rooks haven't moved
    // TODO: Verify king is not in check
    // TODO: Verify castling positions are not under attack

    this.pseudoLegalMoves = moves;
    this.attackingPositions = attacking;
    return moves;
  }
}
