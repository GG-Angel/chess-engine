package chess;

import static chess.MoveGenerator.generateKnightMoves;
import static chess.MoveGenerator.generatePseudoLegalMoves;
import static chess.Piece.Color.BLACK;
import static chess.Piece.Color.WHITE;

import chess.Piece.Type;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
    Board board = new Board();
//    Board board = new Board("r1bqkbnr/ppp1pppp/2n5/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
//    Board board = new Board("8/8/2k2R2/2b5/6K1/5Q2/8/2R5 b - - 0 1");
//    Board board = new Board("8/8/8/8/1b4k1/5np1/3BPp2/q1N1K3 w - - 0 1");
    System.out.println(board);


    long start = System.nanoTime();
    List<Move> pseudoLegalMoves = generatePseudoLegalMoves(board, WHITE);
    long end = System.nanoTime();

    System.out.println("PSEUDO-LEGAL MOVES: " + pseudoLegalMoves);
    System.out.println("# OF MOVES: " + pseudoLegalMoves.size());
    System.out.println("TOOK " + ((end - start) / 1000000.0)+ " ms");

    pseudoLegalMoves.clear();
    generateKnightMoves(
        pseudoLegalMoves,
        board.getPieces(WHITE, Type.KNIGHT),
        board.getPiecesByColor(WHITE),
        board.getPiecesByColor(BLACK)
    );
    System.out.println(pseudoLegalMoves);
  }
}
