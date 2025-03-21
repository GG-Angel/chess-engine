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
  public static void generatePawnMoves(List<Move> moves, Color color, long pawns, long them, long empty) {
    if (color == WHITE) {
      addPawnMoves(moves, pawns, empty, ~rank8, 8, QUIET);
      addPawnMoves(moves, pawns, empty & (empty << 8), rank4, 16, DOUBLE_PAWN_PUSH);
      addPawnMoves(moves, pawns, them, ~(rank8 | fileA), 7, CAPTURE);
      addPawnMoves(moves, pawns, them, ~(rank8 | fileH), 9, CAPTURE);

      addPromotionMoves(moves, pawns, empty, rank8, 8, Promotions);
      addPromotionMoves(moves, pawns, them, rank8 & ~fileH, 9, PromotionCaptures);
      addPromotionMoves(moves, pawns, them, rank8 & ~fileA, 7, PromotionCaptures);
    } else {
      addPawnMoves(moves, pawns, empty, ~rank1, -8, QUIET);
      addPawnMoves(moves, pawns, empty & (empty >> 8), rank5, -16, DOUBLE_PAWN_PUSH);
      addPawnMoves(moves, pawns, them, ~(rank1 | fileH), -9, CAPTURE);
      addPawnMoves(moves, pawns, them, ~(rank1 | fileA), -7, CAPTURE);

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
