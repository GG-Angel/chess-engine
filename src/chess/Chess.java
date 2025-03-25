package chess;

import static chess.MoveGenerator.generatePawnMoves;
import static chess.Piece.Color.BLACK;
import static chess.Piece.Color.WHITE;

import chess.Piece.Type;
import java.util.ArrayList;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
    long start = System.nanoTime();
    Board board = new Board("r1bqkbnr/ppp1pppp/2n5/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
    System.out.println(board);

    List<Move> moves = new ArrayList<>();

    generatePawnMoves(
        moves, WHITE,
        board.getPieces(WHITE, Type.PAWN),
        board.getPiecesByColor(WHITE),
        board.getPiecesByColor(BLACK)
    );
    System.out.println("WHITE: " + moves);

    moves.clear();
    generatePawnMoves(
        moves, BLACK,
        board.getPieces(BLACK, Type.PAWN),
        board.getPiecesByColor(BLACK),
        board.getPiecesByColor(WHITE)
    );
    System.out.println("BLACK: " + moves);

    long end = System.nanoTime();
    System.out.println(((end - start) / 1000000.0)+ " ms");
  }
}
