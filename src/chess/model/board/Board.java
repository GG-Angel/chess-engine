package chess.model.board;

import chess.model.move.Move;
import chess.model.piece.Piece;

import chess.model.piece.PieceColor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public interface Board {

    List<Move> generateMoves(PieceColor side);

    List<Move> generateLegalMoves(PieceColor side);

    boolean generateKingCheck(PieceColor side);

    boolean isKingInCheck(PieceColor side);

    long legalMovesPerft(int depth);

    boolean isValidMove(Move move);

    void validateMove(Move move);

    void makeMove(Move move);

    void undoMove();

    Map<PieceColor, Set<Piece>> getPieces();

    Map<PieceColor, List<Move>> getMoves();

    PieceColor getOpposingColor(PieceColor color);

    boolean isTurnKingInCheck();

    Stack<Move> getMoveStack();

    int getHalfMoves();

    int getFullMoves();

    Piece getPieceAt(int row, int col);

    boolean isOutOfBounds(int row, int col);

    void validateBounds(int row, int col);
}
