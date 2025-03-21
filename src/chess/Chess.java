package chess;

import static chess.Board.bitboardToString;
import static chess.Masks.kingMask;
import static chess.MoveGenerator.generateBishopMoves;
import static chess.MoveGenerator.generateCastlingMoves;
import static chess.MoveGenerator.generateKingMoves;
import static chess.MoveGenerator.generateKnightMoves;
import static chess.MoveGenerator.generatePawnMoves;
import static chess.MoveGenerator.generateQueenMoves;
import static chess.MoveGenerator.generateRookMoves;
import static chess.MoveGenerator.generateUnsafeSquares;

import java.util.ArrayList;
import java.util.List;

public class Chess {
  public static void main(String[] args) {
    long start = System.nanoTime();

    Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(board);

    List<Move> moves = new ArrayList<>();

    System.out.println("Pawns:");
    generatePawnMoves(moves, Color.WHITE, board.getPiecesByType(Piece.WHITE_PAWN), board.getPiecesByColor(Color.WHITE), board.getPiecesByColor(Color.BLACK));
    System.out.println("WHITE: " + moves);

    moves.clear();
    generatePawnMoves(moves, Color.BLACK, board.getPiecesByType(Piece.BLACK_PAWN), board.getPiecesByColor(Color.BLACK), board.getPiecesByColor(Color.WHITE));
    System.out.println("BLACK: " + moves);

    System.out.println("Knights:");
    generateKnightMoves(moves, board.getPiecesByType(Piece.WHITE_KNIGHT), board.getPiecesByColor(Color.WHITE), board.getPiecesByColor(Color.BLACK));
    System.out.println("WHITE: " + moves);

    moves.clear();
    generateKnightMoves(moves, board.getPiecesByType(Piece.BLACK_KNIGHT), board.getPiecesByColor(Color.BLACK), board.getPiecesByColor(Color.WHITE));
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

    long unsafeSquares = generateUnsafeSquares(
        board.getPiecesByType(Piece.WHITE_KING),
        board.getPiecesByColor(Color.WHITE),
        board.getPiecesByColor(Color.BLACK),
        board.getPiecesByType(Piece.BLACK_PAWN),
        board.getPiecesByType(Piece.BLACK_KNIGHT),
        board.getPiecesByType(Piece.BLACK_BISHOP),
        board.getPiecesByType(Piece.BLACK_ROOK),
        board.getPiecesByType(Piece.BLACK_QUEEN),
        board.getPiecesByType(Piece.BLACK_KING)
    );

    System.out.println(board);

    System.out.println("King:");
    moves.clear();
    generateKingMoves(
        moves,
        board.getPiecesByType(Piece.WHITE_KING),
        board.getPiecesByColor(Color.WHITE),
        board.getPiecesByColor(Color.BLACK),
        unsafeSquares
    );
    generateCastlingMoves(
        moves,
        Color.WHITE,
        board.getPiecesByType(Piece.WHITE_KING),
        board.getPiecesByType(Piece.WHITE_ROOK),
        board.getOccupied(),
        unsafeSquares,
        true,
        true
    );
    System.out.println("WHITE: " + moves);

    long end = System.nanoTime();

    System.out.println(((end - start) / 1000000.0)+ " ms");
  }
}
