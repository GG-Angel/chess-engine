package chess.model.piece;

import static chess.model.piece.PieceColor.WHITE;
import static chess.model.piece.PieceColor.getEnemyColor;
import static chess.model.piece.PieceConstants.DIRECTION_OFFSETS;
import static chess.model.piece.PieceConstants.NUM_SQUARES_FROM_EDGE;
import static chess.model.piece.PieceType.ROOK;

import chess.model.Board;
import chess.model.ChessMove;
import chess.model.Move;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChessKing extends ChessPiece {
  private final int[] castleDirections = new int[] { 2, 1, -1, -2, -3 };

  public ChessKing(PieceColor color, int position) {
    super(color, PieceType.KING, position);
  }

  @Override
  public List<Move> calculatePseudoLegalMoves(Board board) {
    List<Move> moves = new ArrayList<>();
    List<Integer> controlled = new ArrayList<>();

    int[] distancesFromEdges = NUM_SQUARES_FROM_EDGE[this.position];

    // calculate surrounding moves
    for (int dirIndex = 0; dirIndex < 8; dirIndex++) {
      if (distancesFromEdges[dirIndex] == 0) {
        continue;
      }

      int targetPosition = this.position + DIRECTION_OFFSETS[dirIndex];
      Piece targetPiece = board.getPieceAtPosition(targetPosition);
      controlled.add(targetPosition);

      if (isEmpty(targetPiece) || !isColor(targetPiece, this.color)) {
        moves.add(new ChessMove(this.position, targetPosition, this));
      }
    }

    // calculate castling moves
    if (!this.hasMoved) {
      Set<Integer> enemyPositionsControlled = board.getPositionsControlled(getEnemyColor(this.color));
      boolean isKingInCheck = enemyPositionsControlled.contains(this.position);

      if (!isKingInCheck) {
        calculateCastlingMoves(this.position + 3, this.position + 1, this.position + 2, 0, 2, enemyPositionsControlled, board, moves);
        calculateCastlingMoves(this.position - 4, this.position - 1, this.position - 2, 2, 5, enemyPositionsControlled, board, moves);
      }
    }

    this.pseudoLegalMoves = moves;
    this.positionsControlled = controlled;
    return moves;
  }

  private void calculateCastlingMoves(
      int rookPosition,
      int rookTargetPosition,
      int kingTargetPosition,
      int startCastleDirIndex,
      int endCastleDirIndex,
      Set<Integer> enemyPositionsControlled,
      Board board,
      List<Move> moves
  ) {
    Piece rook = board.getPieceAtPosition(rookPosition);
    if (isFriendlyRook(rook) && !rook.hasMoved()) {
      // verify that the king path is clear and not controlled by the enemy
      for (int castleDirIndex = startCastleDirIndex; castleDirIndex < endCastleDirIndex; castleDirIndex++) {
        int pathPosition = this.position + castleDirections[castleDirIndex];

        boolean isClearToMoveThrough = isEmpty(board.getPieceAtPosition(pathPosition));
        boolean isControlledByEnemy = enemyPositionsControlled.contains(pathPosition);

        if (!isClearToMoveThrough || isControlledByEnemy) {
          return; // since the path is invalid
        }
      }

      Move rookMove = new ChessMove(rookPosition, rookTargetPosition, rook);
      Move kingMove = new ChessMove(this.position, kingTargetPosition, this, rookMove);
      moves.add(kingMove); // full castling move
    }
  }

  private boolean isFriendlyRook(Piece piece) {
    return !isEmpty(piece) && isType(piece, ROOK) && isColor(piece, this.color);
  }
}
