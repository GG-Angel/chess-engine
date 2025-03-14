package chess.model.piece;

public class PieceFactory {
  public static Piece createPiece(PieceColor color, PieceType type, int position) {
    return switch (type) {
      case PAWN -> new ChessPawn(color, position);
      case KNIGHT -> new ChessKnight(color, position);
      case BISHOP -> new ChessBishop(color, position);
      case ROOK -> new ChessRook(color, position);
      case QUEEN -> new ChessQueen(color, position);
      case KING -> new ChessKing(color, position);
    };
  }
}
