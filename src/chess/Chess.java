package chess;

import static chess.Masks.universe;
import static chess.MoveGenerator.generateKnightMoves;
import static chess.MoveGenerator.generatePawnMoves;
import static chess.MoveGenerator.generatePseudoLegalMoves;
import static chess.Piece.Color.BLACK;
import static chess.Piece.Color.WHITE;

import chess.Piece.Type;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
    Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(board);


    long start = System.nanoTime();
    List<Move> moves = generatePseudoLegalMoves(board, WHITE);
    long end = System.nanoTime();

    System.out.println("PSEUDO-LEGAL MOVES: ");
    for (Move move : moves) {
      System.out.println(move + ": 1");
    }

    System.out.println("# OF MOVES: " + moves.size());
    System.out.println("TOOK " + ((end - start) / 1000000.0)+ " ms");

    moves.clear();
    generatePawnMoves(moves, WHITE, board.getPieces(WHITE, Type.PAWN), board.getPiecesByColor(WHITE), board.getPiecesByColor(BLACK), universe);
    System.out.println(moves);
  }
}
