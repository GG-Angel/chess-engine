package chess.model.piece;

import static chess.model.piece.PieceColor.WHITE;
import static chess.model.piece.PieceConstants.DIRECTION_OFFSETS;
import static chess.model.piece.PieceConstants.NUM_SQUARES_FROM_EDGE;
import static chess.model.piece.PieceType.KNIGHT;
import static chess.model.piece.PieceType.BISHOP;
import static chess.model.piece.PieceType.ROOK;
import static chess.model.piece.PieceType.QUEEN;

import chess.model.Board;
import chess.model.ChessMove;
import chess.model.Move;
import java.util.LinkedList;
import java.util.List;

public class ChessPawn extends ChessPiece {
  private static final PieceType[] promotionPieceTypes = new PieceType[] { KNIGHT, BISHOP, ROOK, QUEEN };

  private final boolean isWhite;
  private final int homeRow, promotionRow;
  private final int direction;
  private final int startCaptureDirIndex;


  public ChessPawn(PieceColor color, int position) {
    super(color, PieceType.PAWN, position);
    this.isWhite = isColor(this, WHITE);
    this.homeRow = isWhite ? 6 : 1;
    this.promotionRow = isWhite ? 0 : 7;
    this.direction = isWhite ? -1 : 1;
    this.startCaptureDirIndex = isWhite ? 4 : 6;
  }

  @Override
  public List<Move> calculatePseudoLegalMoves(Board board) {
    List<Move> moves = new LinkedList<>();
    List<Integer> attacking = new LinkedList<>();

    // get distance from end of board
    int distanceFromEnd = NUM_SQUARES_FROM_EDGE[this.position][isWhite ? 0 : 3];
    if (distanceFromEnd != 0) {
      calculateForwardMoves(board, moves);
      calculateDiagonalCaptures(board, moves, attacking);
    }

    this.attackingPositions = attacking;
    return moves;
  }

  private void calculateForwardMoves(Board board, List<Move> moves) {
    for (int step = 1; step <= 2; step++) {
      int targetPosition = this.position + (8 * direction * step);
      if (targetPosition < 0 || targetPosition >= 64) {
        break;
      }

      Piece targetPiece = board.getPieceAtPosition(targetPosition);
      if (!isEmpty(targetPiece)) {
        break;
      }

      if (step == 1 || this.position / 8 == homeRow) {
        handleCreateMove(targetPosition, moves);
      }
    }
  }

  private void calculateDiagonalCaptures(Board board, List<Move> moves, List<Integer> attacking) {
    int enPassantTargetPosition = board.getEnPassantTarget();
    for (int i = 0; i <= 1; i++) {
      // get distance from left or right board edges
      int edgeDistance = NUM_SQUARES_FROM_EDGE[this.position][i + 1];
      if (edgeDistance >= 1) {
        // get the corresponding target position
        int targetPosition = this.position + DIRECTION_OFFSETS[startCaptureDirIndex + i];
        attacking.add(targetPosition);

        if (enPassantTargetPosition == targetPosition) {
          // handle en passant
          int enPassantPawnPosition = targetPosition + (8 * -direction);
          attacking.add(enPassantPawnPosition);
          moves.add(new ChessMove(this.position, targetPosition, this, enPassantPawnPosition));
        } else {
          // handle regular captures
          Piece targetPiece = board.getPieceAtPosition(targetPosition);
          if (!isEmpty(targetPiece) && !isColor(targetPiece, this.color)) {
            handleCreateMove(targetPosition, moves);
          }
        }
      }
    }
  }

  private void handleCreateMove(int targetPosition, List<Move> moves) {
    if (targetPosition / 8 == promotionRow) {
      for (PieceType promotionPieceType : promotionPieceTypes) {
        moves.add(new ChessMove(position, targetPosition, this, promotionPieceType));
      }
    } else {
      moves.add(new ChessMove(position, targetPosition, this));
    }
  }
}
