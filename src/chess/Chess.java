package chess;

import static chess.MoveGenerator.generateBishopMoves;
import static chess.MoveGenerator.generatePawnMoves;
import static chess.MoveGenerator.generateQueenMoves;
import static chess.MoveGenerator.generateRookMoves;

import java.util.ArrayList;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
    long start = System.currentTimeMillis();

    Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(board);

    List<Move> moves = new ArrayList<>();

    System.out.println("Pawns:");
    generatePawnMoves(moves, Color.WHITE, board.getPiecesByType(Piece.WHITE_PAWN), board.getPiecesByColor(Color.WHITE), board.getPiecesByColor(Color.BLACK));
    System.out.println("WHITE: " + moves);

    moves.clear();
    generatePawnMoves(moves, Color.BLACK, board.getPiecesByType(Piece.BLACK_PAWN), board.getPiecesByColor(Color.BLACK), board.getPiecesByColor(Color.WHITE));
    System.out.println("BLACK: " + moves);

    System.out.println("Rooks:");
    generateRookMoves(moves, board.getPiecesByType(Piece.WHITE_ROOK), board.getPiecesByColor(Color.WHITE), board.getPiecesByColor(Color.BLACK));
    System.out.println("WHITE: " + moves);

    moves.clear();
    generateRookMoves(moves, board.getPiecesByType(Piece.BLACK_ROOK), board.getPiecesByColor(Color.BLACK), board.getPiecesByColor(Color.WHITE));
    System.out.println("BLACK: " + moves);

    System.out.println("Bishops:");
    moves.clear();
    generateBishopMoves(moves, board.getPiecesByType(Piece.WHITE_BISHOP), board.getPiecesByColor(Color.WHITE), board.getPiecesByColor(Color.BLACK));
    System.out.println("WHITE: " + moves);

    moves.clear();
    generateBishopMoves(moves, board.getPiecesByType(Piece.BLACK_BISHOP), board.getPiecesByColor(Color.BLACK), board.getPiecesByColor(Color.WHITE));
    System.out.println("BLACK: " + moves);

    System.out.println("Queens:");
    moves.clear();
    generateQueenMoves(moves, board.getPiecesByType(Piece.WHITE_QUEEN), board.getPiecesByColor(Color.WHITE), board.getPiecesByColor(Color.BLACK));
    System.out.println("WHITE: " + moves);

    moves.clear();
    generateQueenMoves(moves, board.getPiecesByType(Piece.BLACK_QUEEN), board.getPiecesByColor(Color.BLACK), board.getPiecesByColor(Color.WHITE));
    System.out.println("BLACK: " + moves);

    long end = System.currentTimeMillis();

    System.out.println((end - start) + " ms");
  }
}
