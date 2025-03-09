package chess.model.piece;

public class PieceFactory {
  public static ChessPiece createPiece(PieceColor color, PieceType type, int row, int col) {
    return switch (type) {
      case PAWN -> new ChessPawn(color, row, col);
      case BISHOP -> new ChessBishop(color, row, col);
      case KNIGHT -> new ChessKnight(color, row, col);
      case ROOK -> new ChessRook(color, row, col);
      case QUEEN -> new ChessQueen(color, row, col);
      case KING -> new ChessKing(color, row, col);
    };
  }
}

