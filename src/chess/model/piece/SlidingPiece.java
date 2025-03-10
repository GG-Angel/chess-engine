package chess.model.piece;

import static chess.model.piece.PieceConstants.DIRECTION_OFFSETS;
import static chess.model.piece.PieceConstants.NUM_SQUARES_FROM_EDGE;

import chess.model.Board;
import chess.model.ChessMove;
import chess.model.Move;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SlidingPiece extends ChessPiece {
  public SlidingPiece(PieceColor color, PieceType type, int position) {
    super(color, type, position);
  }

  @Override
  public List<Move> calculatePseudoLegalMoves(Board board) {
    List<Move> moves = new LinkedList<>();

    int startDirIndex = isType(this, PieceType.BISHOP) ? 4 : 0;
    int endDirIndex = isType(this, PieceType.ROOK) ? 4 : 8;

    System.out.println(Arrays.toString(NUM_SQUARES_FROM_EDGE[position]));
    System.out.println(position);
    for (int dirIndex = startDirIndex; dirIndex < endDirIndex; dirIndex++) {
      System.out.println(DIRECTION_OFFSETS[dirIndex]);
      for (int dist = 0; dist < NUM_SQUARES_FROM_EDGE[position][dirIndex]; dist++) {
        int targetPosition = position + DIRECTION_OFFSETS[dirIndex] * (dist + 1);
        Piece targetPiece = board.getPieceAtPosition(targetPosition);

        // blocked by friendly piece, stop moving in this direction
        if (isColor(targetPiece, this.color)) break;

        moves.add(new ChessMove(position, targetPosition, targetPiece));

        // can't move further in this direction after capturing enemy piece
        if (!isColor(targetPiece, this.color)) break;
      }
    }

    return moves;
  }
}
