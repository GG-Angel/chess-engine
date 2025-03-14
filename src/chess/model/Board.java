package chess.model;

import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import java.util.List;
import java.util.Set;

public interface Board {

  List<Move> generatePseudoLegalMoves(PieceColor color);

  boolean isKingInCheck(PieceColor color) throws IllegalStateException;

  void makeMove(Move move);

  Set<Integer> getPositionsControlled(PieceColor color);

  Piece getPieceAtPosition(int position);

  int getEnPassantTarget();
}
