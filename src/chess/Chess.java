package chess;

import static chess.Board.bitboardToString;
import static chess.MoveGenerator.generateRookMoves;

import java.util.ArrayList;
import java.util.List;

public class Chess {
  public static void printBitboard(long bitboard) {
    // Loop through each rank (8 ranks total) from the top (rank 8) to the bottom (rank 1)
    for (int rank = 7; rank >= 0; rank--) {
      // Mask to isolate the current rank
      long rankMask = bitboard >> (rank * 8);
      // Extract the last 8 bits for the current rank
      long rankBits = rankMask & 0xFF;

      // Convert to binary and print with padding
      String binary = String.format("%8s", Long.toBinaryString(rankBits)).replace(' ', '0');
      System.out.print(binary + " "); // Print the binary for this rank
    }
    System.out.println();
  }

  public static void main(String[] args) {
    Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(board);

    List<Move> moves = new ArrayList<>();

    generateRookMoves(moves, board.getPiecesByType(Piece.WHITE_ROOK), board.getPiecesByColor(Color.WHITE), board.getPiecesByColor(Color.BLACK));
    System.out.println("WHITE: " + moves);

    moves.clear();
    generateRookMoves(moves, board.getPiecesByType(Piece.BLACK_ROOK), board.getPiecesByColor(Color.BLACK), board.getPiecesByColor(Color.WHITE));
    System.out.println("BLACK: " + moves);
  }
}
