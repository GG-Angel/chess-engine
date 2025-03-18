package chess;

import static chess.Board.bitboardToString;
import static chess.Color.WHITE;
import static chess.Masks.rank4;
import static chess.Masks.rank5;
import static chess.Masks.rank8;

import java.util.List;

public class MoveGenerator {
  public static long generatePawnMoves(List<Move> moves, Color color, long pawns, long them, long occupied) {
    long empty = ~occupied;
    if (color == WHITE) {
      long singlePush = (pawns << 8) & empty & ~rank8;
      System.out.println(bitboardToString(singlePush));

      long doublePush = (pawns << 16) & empty & (empty << 8) & rank4;
      System.out.println(bitboardToString(doublePush));
    } else {
      long singlePush = (pawns >> 8) & empty & ~rank8;
      System.out.println(bitboardToString(singlePush));

      long doublePush = (pawns >> 16) & empty & (empty >> 8) & rank5;
      System.out.println(bitboardToString(doublePush));
    }

    return 0L;
  }
}
