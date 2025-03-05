package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.move.ChessMoveType;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public class ChessKing extends ProximityPiece {

  public ChessKing(PieceColor color) {
    super(color, PieceType.KING);
  }

  @Override
  public List<Move> computeMoves(int fromRow, int fromCol, ChessBoard board) throws IndexOutOfBoundsException {
    int[][] distances = new int[][] {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
    List<Move> moves = computeMoves(fromRow, fromCol, distances, board);
    List<Move> castlingMoves = computeCastling(fromRow, fromCol, board);

    moves.addAll(castlingMoves);
    return moves;
  }

  private List<Move> computeCastling(int fromRow, int fromCol, ChessBoard board) {
    List<Move> moves = new ArrayList<>();
    if (hasMoved || board.isCurrentKingInCheck()) return moves;

    checkCastling(fromRow, fromCol, board, 7, 6, 5, moves); // king-side castling
    checkCastling(fromRow, fromCol, board, 0, 2, 3, moves); // queen-side castling

    return moves;
  }

  private void checkCastling(int fromRow, int fromCol, ChessBoard board, int rookCol, int kingTargetCol, int rookTargetCol, List<Move> moves) {
    Piece rook = board.getPieceAt(fromRow, rookCol);
    if (rook == null || rook.getType() != PieceType.ROOK || isOpposingPiece(rook) || rook.hasMoved()) return;

    int step = (rookCol == 7) ? 1 : -1;
    for (int col = fromCol + step; col != rookCol; col += step) {
      if (board.getPieceAt(fromRow, col) != null) return; // collision detected
    }

    Move rookMove = new ChessMove(fromRow, rookCol, rook, fromRow, rookTargetCol, null);
    Move kingMove = new ChessMove(fromRow, fromCol, this, fromRow, kingTargetCol, null, rookMove, ChessMoveType.CASTLE);
    moves.add(kingMove);
  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
