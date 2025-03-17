package chess;

import java.util.ArrayList;

public class Chess {
  public static void main(String[] args) {
    Board board = new Board();
    System.out.println(board);

    System.out.println(MoveGenerator.generatePawnMoves(new ArrayList<>(), Color.WHITE, board.getPiecesByType(Piece.WHITE_PAWN), board.getPiecesByType(Piece.BLACK_PAWN)));
  }
}
