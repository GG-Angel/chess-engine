package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.move.Move;
import java.util.ArrayList;
import java.util.List;

public abstract class DirectionalPiece extends ChessPiece {

  protected DirectionalPiece(PieceColor color, PieceType type) throws NullPointerException {
    super(color, type);
  }

  protected List<Move> computeMoves(int fromRow, int fromCol, int[][] directions, ChessBoard board) throws IndexOutOfBoundsException {
    board.validateBounds(fromRow, fromCol);
    List<Move> moves = new ArrayList<>();

    for (int[] dir : directions) {
      for (int dist = 1; dist < board.getBoardSize(); dist++) {
        int toRow = fromRow + (dist * dir[0]);
        int toCol = fromCol + (dist * dir[1]);
        if (board.isOutOfBounds(toRow, toCol)) break;

        Piece toPiece = board.getPieceAt(toRow, toCol);
        Move move = new ChessMove(fromRow, fromCol, this, toRow, toCol, toPiece);

        // continue in this direction until we collide or go out of bounds
        if (toPiece == null) {
          moves.add(move);
        } else {
          if (isOpposingPiece(toPiece)) {
            moves.add(move);
          }
          break;
        }
      }
    }

    return moves;
  }
}
