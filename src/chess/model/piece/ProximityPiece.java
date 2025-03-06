package chess.model.piece;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class ProximityPiece extends ChessPiece {

  protected ProximityPiece(PieceColor color, PieceType type, int row, int col) throws NullPointerException {
    super(color, type, row, col);
  }

  protected List<Move> computeMoves(int[][] distances, Board board) throws IndexOutOfBoundsException {
    List<Move> moves = new ArrayList<>();
    for (int[] dist : distances) {
      int toRow = this.row + dist[0];
      int toCol = this.col + dist[1];
      if (board.isOutOfBounds(toRow, toCol)) continue;

      Piece toPiece = board.getPieceAt(toRow, toCol);
      if (toPiece == null || toPiece.getColor() != this.color) {
        Move move = new ChessMove(this.row, this.col, this, toRow, toCol, toPiece);
        moves.add(move);
      }
    }
    return moves;
  }
}
