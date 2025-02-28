package chess.model.piece;

public class PieceFactory {
  public static ChessPiece createPiece(PieceColor color, PieceType type) {
    return switch (type) {
      case PAWN -> new ChessPawn(color);
      case BISHOP -> new ChessBishop(color);
      case KNIGHT -> new ChessKnight(color);
      case ROOK -> new ChessRook(color);
      case QUEEN -> new ChessQueen(color);
      case KING -> new ChessKing(color);
    };
  }
}
