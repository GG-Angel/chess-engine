package chess.model;

import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import java.util.List;

public interface Board {

  List<Move> generatePseudoLegalMoves(PieceColor color);

  void makeMove(Move move);

  Piece getPieceAtPosition(int position);

  int getEnPassantTarget();

  void setEnPassantTarget(int position);
}
