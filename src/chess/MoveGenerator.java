package chess;

import static chess.Color.WHITE;
import static chess.Masks.diagonalRightMasks;
import static chess.Masks.diagonalLeftMasks;
import static chess.Masks.fileA;
import static chess.Masks.fileAB;
import static chess.Masks.fileGH;
import static chess.Masks.fileH;
import static chess.Masks.knightMask;
import static chess.Masks.rank1;
import static chess.Masks.rank4;
import static chess.Masks.rank5;
import static chess.Masks.rank8;
import static chess.Masks.fileMasks;
import static chess.Masks.rankMasks;
import static chess.MoveType.CAPTURE;
import static chess.MoveType.DOUBLE_PAWN_PUSH;
import static chess.MoveType.PromotionCaptures;
import static chess.MoveType.Promotions;
import static chess.MoveType.QUIET;
import static java.lang.Long.reverse;

import java.util.List;

public class MoveGenerator {
  public static void generatePawnMoves(List<Move> moves, Color color, long pawns, long friendly, long enemy) {
    long empty = ~(friendly | enemy);

    int direction = (color == WHITE) ? 1 : -1;
    int shift = 8 * direction;
    long promotionRankMask = (color == WHITE) ? rank8 : rank1;
    long doublePushMask = (color == WHITE) ? rank4 : rank5;

    // quiet moves
    addPawnMoves(moves, pawns, empty, ~promotionRankMask, shift, QUIET);

    // double pawn pushes
    long doublePawnPushTargets = empty & (color == WHITE ? empty << 8 : empty >> 8);
    addPawnMoves(moves, pawns, doublePawnPushTargets, doublePushMask, shift * 2, DOUBLE_PAWN_PUSH);

    // captures
    addPawnMoves(moves, pawns, enemy, ~(promotionRankMask | fileA), 7 * direction, CAPTURE);
    addPawnMoves(moves, pawns, enemy, ~(promotionRankMask | fileH), 9 * direction, CAPTURE);

    // quiet promotions
    addPromotionMoves(moves, pawns, empty, promotionRankMask, shift, Promotions);

    // promotions with capture
    addPromotionMoves(moves, pawns, enemy, promotionRankMask & ~fileA, 7 * direction, PromotionCaptures);
    addPromotionMoves(moves, pawns, enemy, promotionRankMask & ~fileH, 9 * direction, PromotionCaptures);
  }

  private static void addPawnMoves(List<Move> moves, long pawns, long targets, long mask, int shift, MoveType moveType) {
    long pawnMoves = shiftPawns(pawns, shift) & targets & mask;
    while (pawnMoves != 0) {
      int to = Long.numberOfTrailingZeros(pawnMoves);
      int from = to - shift;
      moves.add(new Move(from, to, moveType));
      pawnMoves &= pawnMoves - 1;
    }
  }

  private static void addPromotionMoves(List<Move> moves, long pawns, long targets, long mask, int shift, MoveType[] promotionTypes) {
    long promotionMoves = shiftPawns(pawns, shift) & targets & mask;
    while (promotionMoves != 0) {
      int to = Long.numberOfTrailingZeros(promotionMoves);
      int from = to - shift;
      for (MoveType promotion : promotionTypes) {
        moves.add(new Move(from, to, promotion));
      }
      promotionMoves &= promotionMoves - 1;
    }
  }

  private static long shiftPawns(long bitboard, long amount) {
    return amount >= 0 ? (bitboard << amount) : (bitboard >> -amount);
  }

  public static void generateKnightMoves(List<Move> moves, long knights, long friendly, long enemy) {
    // isolate knight
    long knight = knights & -knights;

    while (knight != 0) {
      int knightSquare = Long.numberOfTrailingZeros(knight);
      long knightMoves;

      // apply move mask to knight position
      if (knightSquare > 18) {
        knightMoves = knightMask << (knightSquare - 18);
      } else {
        knightMoves = knightMask >> (18 - knightSquare);
      }

      // ensure moves do not wrap around the board
      knightMoves &= (knightSquare % 8 < 4) ? ~fileAB : ~fileGH;

      // remove captures of friendly pieces
      knightMoves &= ~friendly;

      createMovesFromBitboard(moves, knightMoves, knightSquare, enemy);

      // isolate next knight
      knights = knights & ~knight;
      knight = knights & -knights;
    }
  }

  public static void generateQueenMoves(List<Move> moves, long queens, long friendly, long enemy) {
    generateRookMoves(moves, queens, friendly, enemy);
    generateBishopMoves(moves, queens, friendly, enemy);
  }

  public static void generateRookMoves(List<Move> moves, long rooks, long friendly, long enemy) {
    long occupied = friendly | enemy;
    while (rooks != 0) {
      int rookSquare = Long.numberOfTrailingZeros(rooks);
      long rook = 1L << rookSquare;

      long straightMoves = generateStraightMoves(rook, occupied, rookSquare);
      long rookMoves = straightMoves & ~friendly;

      createMovesFromBitboard(moves, rookMoves, rookSquare, enemy);
      rooks &= rooks - 1;
    }
  }

  public static void generateBishopMoves(List<Move> moves, long bishops, long friendly, long enemy) {
    long occupied = friendly | enemy;
    while (bishops != 0) {
      int bishopSquare = Long.numberOfTrailingZeros(bishops);
      long bishop = 1L << bishopSquare;

      long diagonalMoves = generateDiagonalMoves(bishop, occupied, bishopSquare);
      long bishopMoves = diagonalMoves & ~friendly;

      createMovesFromBitboard(moves, bishopMoves, bishopSquare, enemy);
      bishops &= bishops - 1;
    }
  }

  private static long generateStraightMoves(long piece, long occupied, int square) {
    long rankMask = rankMasks[square];
    long fileMask = fileMasks[square];

    long rankMoves = calculateSlidingMoves(piece, occupied, rankMask);
    long fileMoves = calculateSlidingMoves(piece, occupied, fileMask);

    return rankMoves | fileMoves;
  }

  private static long generateDiagonalMoves(long piece, long occupied, int square) {
    long leftMask = diagonalLeftMasks[square];
    long rightMask = diagonalRightMasks[square];

    long leftMoves = calculateSlidingMoves(piece, occupied, rightMask);
    long rightMoves = calculateSlidingMoves(piece, occupied, leftMask);

    return leftMoves | rightMoves;
  }

  // hyperbola quintessence :D
  private static long calculateSlidingMoves(long piece, long occupied, long mask) {
    long m0 = (occupied & mask) - (2 * piece);
    long m1 = reverse(reverse(occupied & mask) - (2 * reverse(piece)));
    return (m0 ^ m1) & mask;
  }

  private static void createMovesFromBitboard(List<Move> moves, long movesBitboard, int pieceSquare, long enemy) {
    while (movesBitboard != 0) {
      int bishopTarget = Long.numberOfTrailingZeros(movesBitboard);
      MoveType moveType = (enemy & (1L << bishopTarget)) != 0 ? CAPTURE : QUIET;
      moves.add(new Move(pieceSquare, bishopTarget, moveType));
      movesBitboard &= movesBitboard - 1;
    }
  }

  // TODO: En passant
  // TODO: Castling
  // TODO: King moves
}
