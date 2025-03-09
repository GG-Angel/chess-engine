package chess.model.piece;

import static chess.model.board.ChessBoard.BOARD_SIZE;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class DirectionalPiece extends ChessPiece {

  protected DirectionalPiece(PieceColor color, PieceType type, int row, int col) throws NullPointerException {
    super(color, type, row, col);
  }

  protected List<Move> computeMoves(int[][] directions, Board board) {
    List<Move> moves = new ArrayList<>();
    for (int[] dir : directions) {
      for (int dist = 1; dist < BOARD_SIZE; dist++) {
        // calculate destination square
        int toRow = this.row + (dist * dir[0]);
        int toCol = this.col + (dist * dir[1]);
        if (board.isOutOfBounds(toRow, toCol)) break;

        // if the destination is empty or has an enemy piece, it is a valid move
        Piece toPiece = board.getPieceAt(toRow, toCol);
        if (toPiece == null || isOpposingPiece(toPiece)) {
          Move move = new ChessMove(this.row, this.col, this, toRow, toCol, toPiece);
          moves.add(move);
        }

        // stop if we collide with a friendly piece
        if (toPiece != null) {
          break;
        }
      }
    }
    return moves;
  }
}
