package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.move.Move;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class ProximityPiece extends ChessPiece {
  protected ProximityPiece(PieceColor color, PieceType type) throws NullPointerException {
    super(color, type);
  }

  protected List<Move> computeMoves(int fromRow, int fromCol, int[][] distances, ChessBoard board) throws IndexOutOfBoundsException {
    board.validateBounds(fromRow, fromCol);
    List<Move> moves = new ArrayList<>();

    for (int[] dist : distances) {
      int toRow = fromRow + dist[0];
      int toCol = fromCol + dist[1];
      if (board.isOutOfBounds(toRow, toCol)) continue;

      Piece toPiece = board.getPieceAt(toRow, toCol);
      Move move = new ChessMove(fromRow, fromCol, this, toRow, toCol, toPiece);
      possibleMoves.add(move);

      if (toPiece == null || toPiece.getColor() != this.color) {
        moves.add(move);
      }
    }

    return moves;
  }
}
