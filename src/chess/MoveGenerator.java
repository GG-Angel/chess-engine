package chess;

import static chess.Color.WHITE;
import static chess.Masks.fileA;
import static chess.Masks.fileH;
import static chess.Masks.rank1;
import static chess.Masks.rank4;
import static chess.Masks.rank5;
import static chess.Masks.rank8;
import static chess.MoveType.CAPTURE;
import static chess.MoveType.DOUBLE_PAWN_PUSH;
import static chess.MoveType.PromotionCaptures;
import static chess.MoveType.Promotions;
import static chess.MoveType.QUIET;

import java.util.List;

public class MoveGenerator {
  public static void generatePawnMoves(List<Move> moves, Color color, long pawns, long them, long occupied) {
    long empty = ~occupied;

    if (color == WHITE) {
      addPawnMoves(moves, pawns, empty, ~rank8, 8, QUIET);
      addPawnMoves(moves, pawns, empty & (empty << 8), rank4, 16, DOUBLE_PAWN_PUSH);
      addPawnMoves(moves, pawns, them, ~rank8 & ~fileA, 7, CAPTURE);
      addPawnMoves(moves, pawns, them, ~rank8 & ~fileH, 9, CAPTURE);

      addPromotionMoves(moves, pawns, empty, rank8, 8, Promotions);
      addPromotionMoves(moves, pawns, them, rank8 & ~fileH, 9, PromotionCaptures);
      addPromotionMoves(moves, pawns, them, rank8 & ~fileA, 7, PromotionCaptures);
    }

    else {
      addPawnMoves(moves, pawns, empty, ~rank1, -8, QUIET);
      addPawnMoves(moves, pawns, empty & (empty >> 8), rank5, -16, DOUBLE_PAWN_PUSH);
      addPawnMoves(moves, pawns, them, ~rank1 & ~fileH, -9, CAPTURE);
      addPawnMoves(moves, pawns, them, ~rank1 & ~fileA, -7, CAPTURE);

      addPromotionMoves(moves, pawns, empty, rank1, -8, Promotions);
      addPromotionMoves(moves, pawns, them, rank1 & ~fileH, -9, PromotionCaptures);
      addPromotionMoves(moves, pawns, them, rank1 & ~fileA, -7, PromotionCaptures);
    }
  }


  private static void addPawnMoves(List<Move> moves, long pawns, long targets, long mask, int shift, MoveType moveType) {
    long moveBitboard = shiftPawnBitboard(pawns, shift) & targets & mask;
    while (moveBitboard != 0) {
      int to = Long.numberOfTrailingZeros(moveBitboard);
      int from = to - shift;
      moves.add(new Move(from, to, moveType));
      moveBitboard &= moveBitboard - 1;
    }
  }

  private static void addPromotionMoves(List<Move> moves, long pawns, long targets, long mask, int shift, MoveType[] promotionTypes) {
    long moveBitboard = shiftPawnBitboard(pawns, shift) & targets & mask;
    while (moveBitboard != 0) {
      int to = Long.numberOfTrailingZeros(moveBitboard);
      int from = to - shift;
      for (MoveType promotion : promotionTypes) {
        moves.add(new Move(from, to, promotion));
      }
      moveBitboard &= moveBitboard - 1;
    }
  }

  private static long shiftPawnBitboard(long bitboard, long amount) {
    return amount >= 0 ? (bitboard << amount) : (bitboard >>> -amount);
  }
}
