package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.move.Move;
import java.util.ArrayList;

public abstract class DirectionalPiece extends ChessPiece {

  protected DirectionalPiece(PieceColor color, PieceType type) throws NullPointerException {
    super(color, type);
  }

  protected void computeMoves(int fromRow, int fromCol, int[][] directions, ChessBoard board) throws IndexOutOfBoundsException {
    board.validateBounds(fromRow, fromCol);
    clearMoves();

    for (int[] dir : directions) {
      boolean collision = false;
      for (int dist = 1; dist < board.getBoardSize(); dist++) {
        int toRow = fromRow + dist * dir[0];
        int toCol = fromCol + dist * dir[1];
        if (board.isOutOfBounds(toRow, toCol)) break;

        Piece toPiece = board.getPieceAt(toRow, toCol);
        Move move = new ChessMove(fromRow, fromCol, this, toRow, toCol, toPiece);
        possibleMoves.add(move);

        if (!collision) {
          if (toPiece == null) {
            validMoves.add(move);
            continue;
          }

          // collided with some piece
          if (isOpposingPiece(toPiece)) {
            validMoves.add(move); // capture an enemy piece
          }
          collision = true; // blocked by friendly or enemy piece
        }
      }
    }
  }
}
