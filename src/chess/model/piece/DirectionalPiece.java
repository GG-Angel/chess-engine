package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class DirectionalPiece extends ChessPiece {

  protected DirectionalPiece(PieceColor color, PieceType type, int row, int col) throws NullPointerException {
    super(color, type, row, col);
  }

  protected List<Move> computeMoves(int[][] directions, Board board) throws IndexOutOfBoundsException {
    List<Move> moves = new ArrayList<>();
    for (int[] dir : directions) {
      for (int dist = 1; dist < board.getBoardSize(); dist++) {
        int toRow = this.row + (dist * dir[0]);
        int toCol = this.col + (dist * dir[1]);
        if (board.isOutOfBounds(toRow, toCol)) break;

        Piece toPiece = board.getPieceAt(toRow, toCol);
        Move move = new ChessMove(this.row, this.col, this, toRow, toCol, toPiece);

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
