package chess.model.board;

import chess.model.move.Move;
import chess.model.piece.Piece;

import java.util.List;
import java.util.Stack;

public interface Board {

    List<Move> generateLegalMoves();

    long legalMovesPerft(int depth);

    boolean isValidMove(Move move);

    void validateMove(Move move);

    void makeMove(Move move);

    void undoMove();

    boolean isCurrentKingInCheck();

    Stack<Move> getMoveStack();

    int getBoardSize();

    int getHalfMoves();

    int getFullMoves();

    Piece getPieceAt(int row, int col);

    boolean isOutOfBounds(int row, int col);

    void validateBounds(int row, int col);
}
