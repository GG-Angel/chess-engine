package chess;

import static chess.MoveGenerator.getCheckingPieces;
import static chess.Piece.Color.BLACK;
import static chess.Piece.Color.WHITE;
import static chess.Piece.Type.BISHOP;
import static chess.Piece.Type.KING;
import static chess.Piece.Type.KNIGHT;
import static chess.Piece.Type.PAWN;
import static chess.Piece.Type.QUEEN;
import static chess.Piece.Type.ROOK;

import chess.MoveGenerator.CheckingPiece;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
//    Board board = new Board("r1bqkbnr/ppp1pppp/2n5/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
//    Board board = new Board("8/8/2k2R2/2b5/6K1/5Q2/8/2R5 b - - 0 1");
    Board board = new Board("8/8/8/8/1b4k1/5np1/3BPp2/q1N1K3 w - - 0 1");
    System.out.println(board);

    long king = board.getPieces(WHITE, KING);
    long occupied = board.getOccupied();
    long enemyPawns = board.getPieces(BLACK, PAWN);
    long enemyKnights = board.getPieces(BLACK, KNIGHT);
    long enemyBishops = board.getPieces(BLACK, BISHOP);
    long enemyRooks = board.getPieces(BLACK, ROOK);
    long enemyQueens = board.getPieces(BLACK, QUEEN);

    long start = System.nanoTime();

    List<CheckingPiece> checkingPieces = getCheckingPieces(
        WHITE, king, occupied,
        enemyPawns, enemyKnights,
        enemyBishops, enemyRooks, enemyQueens
    );
    System.out.println("CHECKING PIECES: " + checkingPieces);

    long end = System.nanoTime();
    System.out.println(((end - start) / 1000000.0)+ " ms");
  }
}
