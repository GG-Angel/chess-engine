package chess;

import static chess.Board.printBitboard;
import static chess.Color.WHITE;
import static chess.Masks.blackKSInter;
import static chess.Masks.blackKSRook;
import static chess.Masks.blackQSInter;
import static chess.Masks.blackQSPath;
import static chess.Masks.blackQSRook;
import static chess.Masks.diagonalRightMasks;
import static chess.Masks.diagonalLeftMasks;
import static chess.Masks.fileA;
import static chess.Masks.fileAB;
import static chess.Masks.fileGH;
import static chess.Masks.fileH;
import static chess.Masks.kingMask;
import static chess.Masks.knightMask;
import static chess.Masks.rank1;
import static chess.Masks.rank4;
import static chess.Masks.rank5;
import static chess.Masks.rank8;
import static chess.Masks.fileMasks;
import static chess.Masks.rankMasks;
import static chess.Masks.whiteKSInter;
import static chess.Masks.whiteKSRook;
import static chess.Masks.whiteQSInter;
import static chess.Masks.whiteQSPath;
import static chess.Masks.whiteQSRook;
import static chess.MoveType.CAPTURE;
import static chess.MoveType.DOUBLE_PAWN_PUSH;
import static chess.MoveType.EP_CAPTURE;
import static chess.MoveType.KING_CASTLE;
import static chess.MoveType.PromotionCaptures;
import static chess.MoveType.Promotions;
import static chess.MoveType.QUEEN_CASTLE;
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
    while (knights != 0) {
      int knightSquare = Long.numberOfTrailingZeros(knights);
      long knightMoves = knightMask;

      // apply move mask to knight position
      if (knightSquare > 18) {
        knightMoves <<= (knightSquare - 18);
      } else {
        knightMoves >>= (18 - knightSquare);
      }

      // ensure moves do not wrap around the board
      knightMoves &= (knightSquare % 8 < 4) ? ~fileAB : ~fileGH;

      // remove captures of friendly pieces
      knightMoves &= ~friendly;

      createMovesFromBitboard(moves, knightMoves, knightSquare, enemy);
      knights &= knights - 1;
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

  public static void generateKingMoves(List<Move> moves, long king, long friendly, long enemy, long unsafe) {
    int kingSquare = Long.numberOfTrailingZeros(king);
    long kingMoves = kingMask;

    // move mask to king location
    if (kingSquare > 9) {
      kingMoves <<= kingSquare - 9;
    } else {
      kingMoves >>= 9 - kingSquare;
    }

    // ensure moves do not wrap around the board
    kingMoves &= (kingSquare % 8 < 4) ? ~fileAB : ~fileGH;

    // ensure moves do not enter unsafe squares or capture friendly pieces
    kingMoves &= ~(unsafe | friendly);

    createMovesFromBitboard(moves, kingMoves, kingSquare, enemy);
  }

  public static void generateCastlingMoves(
      List<Move> moves, Color color,
      long king, long rooks, long occupied, long unsafe,
      boolean kingSide, boolean queenSide
  ) {
    // prevent castling when king is in check
    if ((king & unsafe) != 0) return;

    // exit early if castling is not allowed
    if (!kingSide && !queenSide) return;

    int kingSquare = Long.numberOfTrailingZeros(king);
    long ksRook, ksInter, qsRook, qsInter, qsPath;
    int ksTarget, qsTarget;
    if (color == WHITE) {
      ksRook = whiteKSRook;
      ksInter = whiteKSInter;
      qsRook = whiteQSRook;
      qsInter = whiteQSInter;
      qsPath = whiteQSPath;
      ksTarget = 6;
      qsTarget = 2;
    } else {
      ksRook = blackKSRook;
      ksInter = blackKSInter;
      qsRook = blackQSRook;
      qsInter = blackQSInter;
      qsPath = blackQSPath;
      ksTarget = 62;
      qsTarget = 58;
    }

    // check king-side castling
    if (kingSide && (rooks & ksRook) != 0) {
      if (((occupied | unsafe) & ksInter) == 0) {
        moves.add(new Move(kingSquare, ksTarget, KING_CASTLE));
      }
    }

    // check queen-side castling
    if (queenSide && ((rooks & qsRook) != 0)) {
      if ((occupied & qsInter) == 0 && (unsafe & qsPath) == 0) {
        moves.add(new Move(kingSquare, qsTarget, QUEEN_CASTLE));
      }
    }
  }

  public static long generateUnsafeSquares (
      long king, long friendly, long enemy,
      long enemyPawns, long enemyKnights, long enemyBishops,
      long enemyRooks, long enemyQueens, long enemyKing
  ) {
    int kingSquare = Long.numberOfTrailingZeros(king);

    long friendlyWithoutKing = friendly & ~(1L << kingSquare);
    long occupiedWithoutKing = friendlyWithoutKing | enemy;

    // diagonal pawn captures
    long unsafe = (enemyPawns >> 7) & ~fileH;
    unsafe |= (enemyPawns >> 9) & ~fileA;

    // knight captures
    while (enemyKnights != 0) {
      int knightSquare = Long.numberOfTrailingZeros(enemyKnights);
      long knightMoves = knightMask;

      if (knightSquare > 18) {
        knightMoves <<= (knightSquare - 18);
      } else {
        knightMoves >>= (18 - knightSquare);
      }

      knightMoves &= (knightSquare % 8 < 4) ? ~fileAB : ~fileGH;

      unsafe |= knightMoves;
      enemyKnights &= enemyKnights - 1;
    }

    // diagonally sliding pieces
    long enemyDiagonalPieces = enemyBishops | enemyQueens;
    while (enemyDiagonalPieces != 0) {
      int pieceSquare = Long.numberOfTrailingZeros(enemyDiagonalPieces);
      long piece = 1L << pieceSquare;
      long diagonalMoves = generateDiagonalMoves(piece, occupiedWithoutKing, pieceSquare);

      unsafe |= diagonalMoves;
      enemyDiagonalPieces &= enemyDiagonalPieces - 1;
    }

    // straight sliding pieces
    long enemyStraightPieces = enemyRooks | enemyQueens;
    while (enemyStraightPieces != 0) {
      int pieceSquare = Long.numberOfTrailingZeros(enemyStraightPieces);
      long piece = 1L << pieceSquare;
      long straightMoves = generateStraightMoves(piece, occupiedWithoutKing, pieceSquare);

      unsafe |= straightMoves;
      enemyStraightPieces &= enemyStraightPieces - 1;
    }

    // king moves
    int enemyKingSquare = Long.numberOfTrailingZeros(enemyKing);
    long enemyKingMoves = kingMask;
    if (enemyKingSquare > 9) {
      enemyKingMoves <<= (enemyKingSquare - 9);
    } else {
      enemyKingMoves >>= (9 - enemyKingSquare);
    }
    enemyKingMoves &= (enemyKingSquare % 8 < 4) ? ~fileAB : ~fileGH;
    unsafe |= enemyKingMoves;

    return unsafe;
  }

  public static void generateEnPassant(
      List<Move> moves, Color color,
      long friendlyPawns, long enemyPawns, long epFileMask
  ) throws IllegalArgumentException {
    if (epFileMask == 0) return;

    int direction = (color == WHITE) ? 8 : -8;
    long enemyDoublePushRank = (color == WHITE) ? rank5 : rank4;
    long leftOverflowMask = (color == WHITE) ? fileA : fileH;
    long rightOverflowMask = (color == WHITE) ? fileH : fileA;

    if (color == WHITE) {
      long epPawnLeft = ((friendlyPawns >> 1) & enemyPawns) & rank5 & ~fileA & epFileMask;
      long epPawnRight = ((friendlyPawns << 1) & enemyPawns) & rank5 & ~fileH & epFileMask;

      if (epPawnLeft == 0 && epPawnRight == 0) {
        throw new IllegalArgumentException("Failed to find en passant pawn.");
      }

      long epPawn = epPawnLeft != 0 ? epPawnLeft : epPawnRight;
      int epPawnSquare = Long.numberOfTrailingZeros(epPawn);

      int epCapturePawnSquare = epPawnSquare + (epPawnLeft != 0 ? 1 : -1);
      int epCaptureTargetSquare = epPawnSquare + 8;

      moves.add(new Move(epCapturePawnSquare, epCaptureTargetSquare, EP_CAPTURE));
    } else {
      long epPawnLeft = ((friendlyPawns >> 1) & enemyPawns) & rank4 & ~fileH & epFileMask;
      long epPawnRight = ((friendlyPawns << 1) & enemyPawns) & rank4 & ~fileA & epFileMask;

      if (epPawnLeft == 0 && epPawnRight == 0) {
        throw new IllegalArgumentException("Failed to find en passant pawn.");
      }

      long epPawn = epPawnLeft != 0 ? epPawnLeft : epPawnRight;
      int epPawnSquare = Long.numberOfTrailingZeros(epPawn);

      int epCapturePawnSquare = epPawnSquare + (epPawnLeft != 0 ? 1 : -1);
      int epCaptureTargetSquare = epPawnSquare - 8;

      moves.add(new Move(epCapturePawnSquare, epCaptureTargetSquare, EP_CAPTURE));
    }
  }
}
