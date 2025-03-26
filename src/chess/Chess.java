package chess;

import static chess.Masks.universe;
import static chess.Piece.Color.BLACK;
import static chess.Piece.Color.WHITE;
import static chess.Piece.Type.*;

import chess.Piece.Color;
import chess.Piece.Type;
import java.util.ArrayList;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
    Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(board);

    List<Move> moves = new ArrayList<>();
    Color color = WHITE;
    long pawns = board.getPieces(WHITE, PAWN);
    long empty = board.getEmpty();
    long enemy = board.getPiecesByColor(BLACK);

    long start = System.nanoTime();
    MoveGenerator.generatePawnMoves(moves, color, pawns, empty, enemy);
    long end = System.nanoTime();

    System.out.println("PSEUDO-LEGAL MOVES: " + moves);
    System.out.println("# OF MOVES: " + moves.size());
    System.out.println("TOOK " + ((end - start) / 1000000.0)+ " ms");

    moves.clear();
    System.out.println(moves);
  }
}
