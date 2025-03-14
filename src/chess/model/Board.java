package chess.model;

import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import java.util.List;

public interface Board {

  List<Move> generatePseudoLegalMoves(PieceColor color);

  boolean isKingInCheck(PieceColor color) throws IllegalStateException;

  void makeMove(Move move);

  Piece getPieceAtPosition(int position);

  int getEnPassantTarget();
}
