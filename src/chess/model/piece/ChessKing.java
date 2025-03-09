package chess.model.piece;

import static chess.model.move.ChessMoveType.CASTLE;
import static chess.model.piece.PieceType.ROOK;

import chess.model.board.Board;
import chess.model.move.ChessMove;
import chess.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public class ChessKing extends ProximityPiece {
  private static final int[] longCastleCols = new int[] { 3, 2, 1 };
  private static final int[] longCastleKingCols = new int[] { 3, 2 };
  private static final int longCastleRookCol = 0;
  private static final int longCastleRookTargetCol = 3;
  private static final int longCastleKingTargetCol = 2;
  private static final int[] shortCastleCols = new int[] { 5, 6 };
  private static final int shortCastleRookCol = 7;
  private static final int shortCastleRookTargetCol = 5;
  private static final int shortCastleKingTargetCol = 6;

  private final int homeRow;

  public ChessKing(PieceColor color, int row, int col) {
    super(color, PieceType.KING, row, col);
    this.homeRow = this.color == PieceColor.WHITE ? 7 : 0;
    if (this.col != 4) {
      this.hasMovedBefore = true;
    }
  }

  @Override
  public List<Move> computeMoves(Board board) {
    int[][] distances = new int[][] {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
    List<Move> moves = computeMoves(distances, board);
    List<Move> castlingMoves = computeCastling(board);
    moves.addAll(castlingMoves);
    return moves;
  }

  private List<Move> computeCastling(Board board) {
    List<Move> moves = new ArrayList<>();
    if (hasMovedBefore || this.row != homeRow || board.isKingInCheck()) return moves;

    Piece longRook = board.getPieceAt(homeRow, longCastleRookCol);
    Piece shortRook = board.getPieceAt(homeRow, shortCastleRookCol);

    boolean canCastleLong = initialCastleCheck(longRook, longCastleCols, board);
    boolean canCastleShort = initialCastleCheck(shortRook, shortCastleCols, board);

    if (canCastleLong || canCastleShort) {
      PieceColor opponentColor = board.getOpposingColor(this.color);
      List<Move> opponentMoves = board.getMoves().get(opponentColor);
      for (Move opponentMove : opponentMoves) {
        if (opponentMove.toRow() != homeRow) continue;

        int columnAttacked = opponentMove.toCol();
        if (canCastleLong && columnAttacked < 4) {
          canCastleLong = isCastlePathSafe(columnAttacked, longCastleKingCols);
        } else if (canCastleShort && columnAttacked > 4) {
          canCastleShort = isCastlePathSafe(columnAttacked, shortCastleCols);
        }

        if (!canCastleLong && !canCastleShort) break;
      }
    }

    if (canCastleLong) {
      Move rookMove = new ChessMove(this.row, longCastleRookCol, longRook, this.row, longCastleRookTargetCol, null, CASTLE);
      Move kingMove = new ChessMove(this.row, this.col, this, this.row, longCastleKingTargetCol, null, CASTLE, rookMove);
      moves.add(kingMove);
    }

    if (canCastleShort) {
      Move rookMove = new ChessMove(this.row, shortCastleRookCol, shortRook, this.row, shortCastleRookTargetCol, null, CASTLE);
      Move kingMove = new ChessMove(this.row, this.col, this, this.row, shortCastleKingTargetCol, null, CASTLE, rookMove);
      moves.add(kingMove);
    }

    return moves;
  }

  private boolean initialCastleCheck(Piece piece, int[] columns, Board board) {
    return piece != null && piece.getType() == ROOK && piece.getColor() == this.color
        && !piece.hasMovedBefore() && areCastleSlotsEmpty(columns, board);
  }

  private boolean areCastleSlotsEmpty(int[] columns, Board board) {
    for (int col : columns) {
      if (board.getPieceAt(homeRow, col) != null) {
        return false;
      }
    }
    return true;
  }

  private boolean isCastlePathSafe(int columnAttacked, int[] kingPathColumns) {
    for (int shortCol : kingPathColumns) {
      if (columnAttacked == shortCol) {
        return false;
      }
    }
    return true;
  }

//  private void checkCastling(Board board, int rookCol, int kingTargetCol, int rookTargetCol, List<Move> moves) {
//    Piece rook = board.getPieceAt(homeRow, rookCol);
//
//    // ensure we are castling with a friendly rook
//    if (rook == null || rook.getType() != ROOK || rook.hasMovedBefore() || isOpposingPiece(rook)) return;
//
//    // check that the castling path is uninterrupted
//    int step = (rookCol == 7) ? 1 : -1;
//    for (int col = this.col + step; col != rookCol; col += step) {
//      if (board.getPieceAt(homeRow, col) != null) return;
//    }
//
//    // check that the castling path is not being attacked
//    PieceColor opponentColor = board.getOpposingColor(this.color);
//    List<Move> opponentMoves = board.getMoves().get(opponentColor);
//    for (Move opponentMove : opponentMoves) {
//      if (opponentMove.toRow() == homeRow) {
//        for (int col = this.col + step; col != this.col + (step * 3); col += step) {
//          if (opponentMove.toCol() == col) {
//            return;
//          }
//        }
//      }
//    }
//
//    Move rookMove = new ChessMove(this.row, rookCol, rook, this.row, rookTargetCol, null, CASTLE);
//    Move kingMove = new ChessMove(this.row, this.col, this, this.row, kingTargetCol, null, CASTLE, rookMove);
//    moves.add(kingMove);
//  }

  @Override
  public String toString() {
    return this.color == PieceColor.WHITE ? "♔" : "♚";
  }
}
