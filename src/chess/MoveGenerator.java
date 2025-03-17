package chess;

import static chess.Board.bitboardToString;
import static chess.Color.WHITE;

import java.util.List;

public class MoveGenerator {
  public static long generatePawnMoves(List<Move> moves, Color color, long us, long them) {
    if (color == WHITE) {
      System.out.println(bitboardToString(us));
      long singlePush = us << 8;
      System.out.println(bitboardToString(singlePush));

      System.out.println(Long.toBinaryString(us));
      System.out.println(Long.toBinaryString(singlePush));
    }

    return 0L;
  }
}
