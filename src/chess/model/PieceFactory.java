package chess.model;

import chess.model.piece.ChessBishop;
import chess.model.piece.ChessKing;
import chess.model.piece.ChessKnight;
import chess.model.piece.ChessPawn;
import chess.model.piece.ChessQueen;
import chess.model.piece.ChessRook;

public class PieceFactory {
  public static Piece createPiece(PieceColor color, PieceType type, int x, int y) {
    return switch (type) {
      case PAWN -> new ChessPawn(color, x, y);
      case KNIGHT -> new ChessKnight(color, x, y);
      case BISHOP -> new ChessBishop(color, x, y);
      case ROOK -> new ChessRook(color, x, y);
      case QUEEN -> new ChessQueen(color, x, y);
      case KING -> new ChessKing(color, x, y);
    };
  }
}
