package chess;

import static chess.Board.bitboardToString;
import static chess.Color.WHITE;

import java.util.List;

public class MoveGenerator {
  public static long generatePawnMoves(List<Move> moves, Color color, long pawns, long them) {
    if (color == WHITE) {
      System.out.println(bitboardToString(pawns));
      long singlePush = pawns << 8;
      System.out.println(bitboardToString(singlePush));
    } else {
      long singlePush = pawns >> 8;
    }

    return 0L;
  }
}
