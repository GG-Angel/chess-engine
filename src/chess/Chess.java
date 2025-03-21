package chess;

import static chess.MoveGenerator.generateBishopMoves;
import static chess.MoveGenerator.generateRookMoves;

import java.util.ArrayList;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
    Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(board);

    List<Move> moves = new ArrayList<>();

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
  }
}
