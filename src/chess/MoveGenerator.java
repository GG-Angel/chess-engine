package chess;

import static chess.Board.printBitboard;
import static chess.Masks.diagonalRightMasks;
import static chess.Masks.diagonalLeftMasks;
import static chess.Masks.fileA;
import static chess.Masks.fileAB;
import static chess.Masks.fileGH;
import static chess.Masks.fileH;
import static chess.Masks.knightMask;
import static chess.Masks.fileMasks;
import static chess.Masks.rank1;
import static chess.Masks.rank4;
import static chess.Masks.rank5;
import static chess.Masks.rank8;
import static chess.Masks.rankMasks;
import static chess.Masks.universe;
import static chess.MoveType.CAPTURE;
import static chess.MoveType.DOUBLE_PAWN_PUSH;
import static chess.MoveType.PromotionCaptures;
import static chess.MoveType.Promotions;
import static chess.MoveType.QUIET;
import static chess.Piece.Color.WHITE;
import static chess.Piece.Type.BISHOP;
import static chess.Piece.Type.QUEEN;
import static chess.Piece.Type.ROOK;
import static java.lang.Long.reverse;

import chess.Piece.Color;
import chess.Piece.Type;
import java.util.List;

public class MoveGenerator {
  public static void generatePawnMoves(List<Move> moves, Color color, long pawns, long empty, long enemy) {
    generatePawnMoves(moves, color, pawns, empty, enemy, universe);
  }

  private static void generatePawnMoves(List<Move> moves, Color color, long pawns, long empty, long enemy, long constraint) {
    long promotionRank = color == WHITE ? rank8 : rank1;
    long doublePushRank = color == WHITE ? rank4 : rank5;
    long diagonalOverflow1 = color == WHITE ? fileH : fileA;
    long diagonalOverflow2 = color == WHITE ? fileA : fileH;
    int direction = color == WHITE ? 1 : -1;

    long forward1 = shiftBoard(pawns, 8 * direction);
    long forward2 = shiftBoard(pawns, 16 * direction);
    long diagonal1 = shiftBoard(pawns, 7 * direction);
    long diagonal2 = shiftBoard(pawns, 9 * direction);

    long quiets = forward1 & empty & ~promotionRank & constraint;
    long doublePushes = forward2 & (empty & shiftBoard(empty, 8 * direction)) & doublePushRank & constraint;
    long captures1 = diagonal1 & enemy & ~(promotionRank | diagonalOverflow1) & constraint;
    long captures2 = diagonal2 & enemy & ~(promotionRank | diagonalOverflow2) & constraint;

    extractPawnMoves(moves, quiets, 8 * direction, QUIET);
    extractPawnMoves(moves, doublePushes, 16 * direction, DOUBLE_PAWN_PUSH);
    extractPawnMoves(moves, captures1, 7 * direction, CAPTURE);
    extractPawnMoves(moves, captures2, 9 * direction, CAPTURE);

    long promotions = forward1 & empty & promotionRank & constraint;
    long promotionCaptures1 = diagonal1 & enemy & promotionRank & ~diagonalOverflow1 & constraint;
    long promotionCaptures2 = diagonal2 & enemy & promotionRank & ~diagonalOverflow2 & constraint;

    extractPawnPromotions(moves, promotions, 8 * direction, Promotions);
    extractPawnPromotions(moves, promotionCaptures1, 7 * direction, PromotionCaptures);
    extractPawnPromotions(moves, promotionCaptures2, 9 * direction, PromotionCaptures);
  }

  private static void generateKnightMoves(List<Move> moves, long knights, long friendly, long enemy) {
    generateKnightMoves(moves, knights, friendly, enemy, universe);
  }

  private static void generateKnightMoves(List<Move> moves, long knights, long friendly, long enemy, long constraint) {
    while (knights != 0) {
      int knightSquare = Long.numberOfTrailingZeros(knights);
      long knightMoves = calculateKnightMoves(knightSquare);

      // remove captures of friendly pieces
      knightMoves &= ~friendly & constraint;

      extractMoves(moves, knightMoves, knightSquare, enemy);
      knights &= knights - 1;
    }
  }

  private static void generateBishopMoves(List<Move> moves, long bishops, long friendly, long enemy) {
    generateSlidingPieceMoves(moves, bishops, BISHOP, friendly, enemy, universe);
  }

  private static void generateRookMoves(List<Move> moves, long rooks, long friendly, long enemy) {
    generateSlidingPieceMoves(moves, rooks, ROOK, friendly, enemy, universe);
  }

  private static void generateQueenMoves(List<Move> moves, long queens, long friendly, long enemy) {
    generateSlidingPieceMoves(moves, queens, QUEEN, friendly, enemy, universe);
  }

  private static void generateBishopMoves(List<Move> moves, long bishops, long friendly, long enemy, long constraint) {
    generateSlidingPieceMoves(moves, bishops, BISHOP, friendly, enemy, constraint);
  }

  private static void generateRookMoves(List<Move> moves, long rooks, long friendly, long enemy, long constraint) {
    generateSlidingPieceMoves(moves, rooks, ROOK, friendly, enemy, constraint);
  }

  private static void generateQueenMoves(List<Move> moves, long queens, long friendly, long enemy, long constraint) {
    generateSlidingPieceMoves(moves, queens, QUEEN, friendly, enemy, constraint);
  }

  private static void generateSlidingPieceMoves(List<Move> moves, long pieces, Type pieceType, long friendly, long enemy, long constraint) {
    long occupied = friendly | enemy;
    while (pieces != 0) {
      int pieceSquare = Long.numberOfTrailingZeros(pieces);
      long piece = 1L << pieceSquare;

      long pieceMoves = 0L;
      switch (pieceType) {
        case BISHOP -> pieceMoves |= calculateDiagonalMoves(piece, pieceSquare, occupied);
        case ROOK -> pieceMoves |= calculateStraightMoves(piece, pieceSquare, occupied);
        case QUEEN -> pieceMoves |= calculateOmnidirectionalMoves(piece, pieceSquare, occupied);
      }
      pieceMoves &= ~friendly & constraint;

      extractMoves(moves, pieceMoves, pieceSquare, enemy);
      pieces &= pieces - 1;
    }
  }

  private static long calculateKnightMoves(long knight) {
    int knightSquare = Long.numberOfTrailingZeros(knight);
    long knightMoves = knightMask;

    // apply move mask to knight position
    if (knightSquare > 18) {
      knightMoves <<= (knightSquare - 18);
    } else {
      knightMoves >>= (18 - knightSquare);
    }

    // ensure moves do not wrap around the board
    knightMoves &= (knightSquare % 8 < 4) ? ~fileGH : ~fileAB;

    return knightMoves;
  }

  private static long calculateOmnidirectionalMoves(long piece, int pieceSquare, long occupied) {
    return calculateStraightMoves(piece, pieceSquare, occupied) | calculateDiagonalMoves(piece, pieceSquare, occupied);
  }

  private static long calculateStraightMoves(long piece, int pieceSquare, long occupied) {
    long rankMask = rankMasks[pieceSquare];
    long fileMask = fileMasks[pieceSquare];

    long rankMoves = calculateSlidingMoves(piece, occupied, rankMask);
    long fileMoves = calculateSlidingMoves(piece, occupied, fileMask);

    return rankMoves | fileMoves;
  }

  private static long calculateDiagonalMoves(long piece, int pieceSquare, long occupied) {
    long leftMask = diagonalLeftMasks[pieceSquare];
    long rightMask = diagonalRightMasks[pieceSquare];

    long leftMoves = calculateSlidingMoves(piece, occupied, rightMask);
    long rightMoves = calculateSlidingMoves(piece, occupied, leftMask);

    return leftMoves | rightMoves;
  }

  // hyperbola quintessence :D
  private static long calculateSlidingMoves(long piece, long occupied, long rayMask) {
    long m0 = (occupied & rayMask) - (2 * piece);
    long m1 = reverse(reverse(occupied & rayMask) - (2 * reverse(piece)));
    return (m0 ^ m1) & rayMask;
  }

  private static void extractMoves(List<Move> moves, long pieceMoves, int pieceSquare, long enemyPieces) {
    while (pieceMoves != 0) {
      int target = Long.numberOfTrailingZeros(pieceMoves);
      MoveType moveType = (enemyPieces & (1L << target)) != 0 ? CAPTURE : QUIET;
      moves.add(new Move(pieceSquare, target, moveType));
      pieceMoves &= pieceMoves - 1;
    }
  }

  private static void extractPawnMoves(List<Move> moves, long pawnMoves, int reverse, MoveType moveType) {
    while (pawnMoves != 0) {
      int to = Long.numberOfTrailingZeros(pawnMoves);
      int from = to - reverse;
      moves.add(new Move(from, to, moveType));
      pawnMoves &= pawnMoves - 1;
    }
  }

  private static void extractPawnPromotions(List<Move> moves, long promotionMoves, int reverse, MoveType[] promotionTypes) {
    while (promotionMoves != 0) {
      int to = Long.numberOfTrailingZeros(promotionMoves);
      int from = to - reverse;
      for (MoveType promotion : promotionTypes) {
        moves.add(new Move(from, to, promotion));
      }
      promotionMoves &= promotionMoves - 1;
    }
  }

  private static long shiftBoard(long bitboard, long amount) {
    return amount >= 0 ? bitboard << amount : bitboard >> -amount;
  }

//  public static void generatePawnMoves(List<Move> moves, Color color, long pawns, long friendly, long enemy, long constraint) {
//    long empty = ~(friendly | enemy);
//
//    long promotionRankMask = (color == WHITE) ? rank8 : rank1;
//    long doublePushMask = (color == WHITE) ? rank4 : rank5;
//    int direction = (color == WHITE) ? 1 : -1;
//    int shift = 8 * direction;
//
//    // quiet moves
//    addPawnMoves(moves, pawns, empty, ~promotionRankMask, constraint, shift, QUIET);
//
//    // double pawn pushes
//    long doublePawnPushTargets = empty & (color == WHITE ? empty << 8 : empty >> 8);
//    addPawnMoves(moves, pawns, doublePawnPushTargets, doublePushMask, constraint, shift * 2, DOUBLE_PAWN_PUSH);
//
//    // captures
//    addPawnMoves(moves, pawns, enemy, ~(promotionRankMask | fileA), constraint, 7 * direction, CAPTURE);
//    addPawnMoves(moves, pawns, enemy, ~(promotionRankMask | fileH), constraint, 9 * direction, CAPTURE);
//
//    // quiet promotions
//    addPromotionMoves(moves, pawns, empty, promotionRankMask, constraint, shift, Promotions);
//
//    // promotions with capture
//    addPromotionMoves(moves, pawns, enemy, promotionRankMask & ~fileA, constraint, 7 * direction, PromotionCaptures);
//    addPromotionMoves(moves, pawns, enemy, promotionRankMask & ~fileH, constraint, 9 * direction, PromotionCaptures);
//  }
//
//  private static void addPawnMoves(List<Move> moves, long pawns, long targets, long mask, long constraint, int shift, MoveType moveType) {
//    long pawnMoves = shiftPawns(pawns, shift) & targets & mask;
//    while (pawnMoves != 0) {
//      int to = Long.numberOfTrailingZeros(pawnMoves);
//      int from = to - shift;
//      moves.add(new Move(from, to, moveType));
//      pawnMoves &= pawnMoves - 1;
//    }
//  }
//
//  private static void addPromotionMoves(List<Move> moves, long pawns, long targets, long mask, long constraint, int shift, MoveType[] promotionTypes) {
//    long promotionMoves = shiftPawns(pawns, shift) & targets & mask;
//    while (promotionMoves != 0) {
//      int to = Long.numberOfTrailingZeros(promotionMoves);
//      int from = to - shift;
//      for (MoveType promotion : promotionTypes) {
//        moves.add(new Move(from, to, promotion));
//      }
//      promotionMoves &= promotionMoves - 1;
//    }
//  }
//
//  private static long shiftPawns(long bitboard, long amount) {
//    return amount >= 0 ? (bitboard << amount) : (bitboard >> -amount);
//  }
//
//  public static void generateEnPassantMoves(
//      List<Move> moves, Color color,
//      long friendlyPawns, long enemyPawns, long epFileMask, long constraint
//  ) throws IllegalArgumentException {
//    if (epFileMask == 0) return;
//
//    int direction = (color == WHITE) ? 8 : -8;
//    long enemyDoublePushRank = (color == WHITE) ? rank5 : rank4;
//    long leftOverflowMask = (color == WHITE) ? fileA : fileH;
//    long rightOverflowMask = (color == WHITE) ? fileH : fileA;
//
//    long epPawnLeft = ((friendlyPawns >> 1) & enemyPawns) & enemyDoublePushRank & ~leftOverflowMask & epFileMask & constraint;
//    long epPawnRight = ((friendlyPawns << 1) & enemyPawns) & enemyDoublePushRank & ~rightOverflowMask & epFileMask & constraint;
//
//    if (epPawnLeft == 0 && epPawnRight == 0) {
//      throw new IllegalArgumentException("Failed to find en passant pawn, likely due to invalid mask.");
//    }
//
//    long epPawn = epPawnLeft != 0 ? epPawnLeft : epPawnRight;
//    int epPawnSquare = Long.numberOfTrailingZeros(epPawn);
//
//    int epCapturePawnSquare = epPawnSquare + (epPawnLeft != 0 ? 1 : -1);
//    int epCaptureTargetSquare = epPawnSquare + direction;
//
//    moves.add(new Move(epCapturePawnSquare, epCaptureTargetSquare, EP_CAPTURE));
//  }
//
//  private static long calculateKnightMoves(int square) {
//    long knightMoves = knightMask;
//
//    // apply move mask to knight position
//    if (square > 18) {
//      knightMoves <<= (square - 18);
//    } else {
//      knightMoves >>= (18 - square);
//    }
//
//    // ensure moves do not wrap around the board
//    knightMoves &= (square % 8 < 4) ? ~fileGH : ~fileAB;
//
//    return knightMoves;
//  }
//
//  public static void generateKnightMoves(List<Move> moves, long knights, long friendly, long enemy, long constraint) {
//    while (knights != 0) {
//      int knightSquare = Long.numberOfTrailingZeros(knights);
//      long knightMoves = calculateKnightMoves(knightSquare);
//
//      // remove captures of friendly pieces
//      knightMoves &= ~friendly & constraint;
//
//      createMovesFromBitboard(moves, knightMoves, knightSquare, enemy);
//      knights &= knights - 1;
//    }
//  }
//
//  public static void generateQueenMoves(List<Move> moves, long queens, long friendly, long enemy, long constraint) {
//    generateRookMoves(moves, queens, friendly, enemy, constraint);
//    generateBishopMoves(moves, queens, friendly, enemy, constraint);
//  }
//
//  public static void generateRookMoves(List<Move> moves, long rooks, long friendly, long enemy, long constraint) {
//    long occupied = friendly | enemy;
//    while (rooks != 0) {
//      int rookSquare = Long.numberOfTrailingZeros(rooks);
//      long rook = 1L << rookSquare;
//
//      long straightMoves = calculateStraightMoves(rook, occupied, rookSquare);
//      long rookMoves = straightMoves & ~friendly & constraint;
//
//      createMovesFromBitboard(moves, rookMoves, rookSquare, enemy);
//      rooks &= rooks - 1;
//    }
//  }
//
//  public static void generateBishopMoves(List<Move> moves, long bishops, long friendly, long enemy, long constraint) {
//    long occupied = friendly | enemy;
//    while (bishops != 0) {
//      int bishopSquare = Long.numberOfTrailingZeros(bishops);
//      long bishop = 1L << bishopSquare;
//
//      long diagonalMoves = calculateDiagonalMoves(bishop, occupied, bishopSquare);
//      long bishopMoves = diagonalMoves & ~friendly & constraint;
//
//      createMovesFromBitboard(moves, bishopMoves, bishopSquare, enemy);
//      bishops &= bishops - 1;
//    }
//  }
//
//  private static long calculateStraightMoves(long piece, long occupied, int square) {
//    long rankMask = rankMasks[square];
//    long fileMask = fileMasks[square];
//
//    long rankMoves = calculateSlidingMoves(piece, occupied, rankMask);
//    long fileMoves = calculateSlidingMoves(piece, occupied, fileMask);
//
//    return rankMoves | fileMoves;
//  }
//
//  private static long calculateDiagonalMoves(long piece, long occupied, int square) {
//    long leftMask = diagonalLeftMasks[square];
//    long rightMask = diagonalRightMasks[square];
//
//    long leftMoves = calculateSlidingMoves(piece, occupied, rightMask);
//    long rightMoves = calculateSlidingMoves(piece, occupied, leftMask);
//
//    return leftMoves | rightMoves;
//  }
//
//  // hyperbola quintessence :D
//  private static long calculateSlidingMoves(long piece, long occupied, long mask) {
//    long m0 = (occupied & mask) - (2 * piece);
//    long m1 = reverse(reverse(occupied & mask) - (2 * reverse(piece)));
//    return (m0 ^ m1) & mask;
//  }
//
//  private static void createMovesFromBitboard(List<Move> moves, long movesBitboard, int pieceSquare, long enemy) {
//    while (movesBitboard != 0) {
//      int target = Long.numberOfTrailingZeros(movesBitboard);
//      MoveType moveType = (enemy & (1L << target)) != 0 ? CAPTURE : QUIET;
//      moves.add(new Move(pieceSquare, target, moveType));
//      movesBitboard &= movesBitboard - 1;
//    }
//  }
//
//  public static void generateKingMoves(List<Move> moves, long king, long friendly, long enemy, long unsafe) {
//    int kingSquare = Long.numberOfTrailingZeros(king);
//    long kingMoves = kingMask;
//
//    // move mask to king location
//    if (kingSquare > 9) {
//      kingMoves <<= kingSquare - 9;
//    } else {
//      kingMoves >>= 9 - kingSquare;
//    }
//
//    // ensure moves do not wrap around the board
//    kingMoves &= (kingSquare % 8 < 4) ? ~fileAB : ~fileGH;
//
//    // ensure moves do not enter unsafe squares or capture friendly pieces
//    kingMoves &= ~(unsafe | friendly);
//
//    createMovesFromBitboard(moves, kingMoves, kingSquare, enemy);
//  }
//
//  public static void generateCastlingMoves(
//      List<Move> moves, Color color,
//      long king, long rooks, long occupied, long unsafe,
//      boolean kingSide, boolean queenSide
//  ) {
//    // prevent castling when king is in check
//    if ((king & unsafe) != 0) return;
//
//    // exit early if castling is not allowed
//    if (!kingSide && !queenSide) return;
//
//    int kingSquare = Long.numberOfTrailingZeros(king);
//    long ksRook, ksInter, qsRook, qsInter, qsPath;
//    int ksTarget, qsTarget;
//    if (color == WHITE) {
//      ksRook = whiteKSRook;
//      ksInter = whiteKSInter;
//      qsRook = whiteQSRook;
//      qsInter = whiteQSInter;
//      qsPath = whiteQSPath;
//      ksTarget = 6;
//      qsTarget = 2;
//    } else {
//      ksRook = blackKSRook;
//      ksInter = blackKSInter;
//      qsRook = blackQSRook;
//      qsInter = blackQSInter;
//      qsPath = blackQSPath;
//      ksTarget = 62;
//      qsTarget = 58;
//    }
//
//    // check king-side castling
//    if (kingSide && (rooks & ksRook) != 0) {
//      if (((occupied | unsafe) & ksInter) == 0) {
//        moves.add(new Move(kingSquare, ksTarget, KING_CASTLE));
//      }
//    }
//
//    // check queen-side castling
//    if (queenSide && ((rooks & qsRook) != 0)) {
//      if ((occupied & qsInter) == 0 && (unsafe & qsPath) == 0) {
//        moves.add(new Move(kingSquare, qsTarget, QUEEN_CASTLE));
//      }
//    }
//  }
//
//  public static long generateUnsafeSquares (
//      long king, long friendly, long enemy,
//      long enemyPawns, long enemyKnights, long enemyBishops,
//      long enemyRooks, long enemyQueens, long enemyKing
//  ) {
//    int kingSquare = Long.numberOfTrailingZeros(king);
//
//    long friendlyWithoutKing = friendly & ~(1L << kingSquare);
//    long occupiedWithoutKing = friendlyWithoutKing | enemy;
//
//    // diagonal pawn captures
//    long unsafe = (enemyPawns >> 7) & ~fileH;
//    unsafe |= (enemyPawns >> 9) & ~fileA;
//
//    // knight captures
//    while (enemyKnights != 0) {
//      int knightSquare = Long.numberOfTrailingZeros(enemyKnights);
//      long knightMoves = calculateKnightMoves(knightSquare);
//
//      unsafe |= knightMoves;
//      enemyKnights &= enemyKnights - 1;
//    }
//
//    // diagonally sliding pieces
//    long enemyDiagonalPieces = enemyBishops | enemyQueens;
//    while (enemyDiagonalPieces != 0) {
//      int pieceSquare = Long.numberOfTrailingZeros(enemyDiagonalPieces);
//      long piece = 1L << pieceSquare;
//      long diagonalMoves = calculateDiagonalMoves(piece, occupiedWithoutKing, pieceSquare);
//
//      unsafe |= diagonalMoves;
//      enemyDiagonalPieces &= enemyDiagonalPieces - 1;
//    }
//
//    // straight sliding pieces
//    long enemyStraightPieces = enemyRooks | enemyQueens;
//    while (enemyStraightPieces != 0) {
//      int pieceSquare = Long.numberOfTrailingZeros(enemyStraightPieces);
//      long piece = 1L << pieceSquare;
//      long straightMoves = calculateStraightMoves(piece, occupiedWithoutKing, pieceSquare);
//
//      unsafe |= straightMoves;
//      enemyStraightPieces &= enemyStraightPieces - 1;
//    }
//
//    // king moves
//    int enemyKingSquare = Long.numberOfTrailingZeros(enemyKing);
//    long enemyKingMoves = kingMask;
//    if (enemyKingSquare > 9) {
//      enemyKingMoves <<= (enemyKingSquare - 9);
//    } else {
//      enemyKingMoves >>= (9 - enemyKingSquare);
//    }
//    enemyKingMoves &= (enemyKingSquare % 8 < 4) ? ~fileAB : ~fileGH;
//    unsafe |= enemyKingMoves;
//
//    return unsafe;
//  }
//
//  public record CheckingPiece(int square, Type type) {
//    @Override
//    public String toString() {
//      char file = (char) ('a' + (square % 8));
//      int rank = square / 8 + 1;
//      return String.format("(%c%d, %s)", file, rank, type);
//    }
//  }
//
//  public static List<CheckingPiece> getCheckingPieces(
//      Color color, long king, long occupied,
//      long enemyPawns, long enemyKnights,
//      long enemyBishops, long enemyRooks, long enemyQueens
//  ) {
//    List<CheckingPiece> checkingPieces = new ArrayList<>();
//
//    long checkingPawns = 0L;
//    int pawnDirection = color == WHITE ? 1 : -1;
//    if ((color == WHITE && (king & rank8) == 0) || (color == BLACK && (king & rank1) == 0)) {
//      checkingPawns |= shiftPawns(king, 7 * pawnDirection) & ~fileA;
//      checkingPawns |= shiftPawns(king, 9 * pawnDirection) & ~fileH;
//    } // TODO: might be buggy
//
//    int kingSquare = Long.numberOfTrailingZeros(king);
//    long diagonals = calculateDiagonalMoves(king, occupied, kingSquare) & (enemyBishops | enemyQueens);
//    long straights = calculateStraightMoves(king, occupied, kingSquare) & (enemyRooks | enemyQueens);
//
//    checkingPawns &= enemyPawns;
//    long checkingKnights = calculateKnightMoves(kingSquare) & enemyKnights;
//    long checkingBishops = diagonals & enemyBishops;
//    long checkingRooks = straights & enemyRooks;
//    long checkingQueens = (diagonals | straights) & enemyQueens;
//
//    addCheckingPiece(checkingPieces, checkingPawns, PAWN);
//    addCheckingPiece(checkingPieces, checkingKnights, KNIGHT);
//    addCheckingPiece(checkingPieces, checkingBishops, BISHOP);
//    addCheckingPiece(checkingPieces, checkingRooks, ROOK);
//    addCheckingPiece(checkingPieces, checkingQueens, QUEEN);
//
//    return checkingPieces;
//  }
//
//  private static void addCheckingPiece(List<CheckingPiece> checkingPieces, long piece, Type type) {
//    while (piece != 0) {
//      int square = Long.numberOfTrailingZeros(piece);
//      checkingPieces.add(new CheckingPiece(square, type));
//      piece &= piece - 1;
//    }
//  }
//
//  public static List<Move> generatePseudoLegalMoves(Board board, Color color) {
//    List<Move> moves = new ArrayList<>();
//
//    long pawns = board.getPieces(color, PAWN);
//    long knights = board.getPieces(color, KNIGHT);
//    long bishops = board.getPieces(color, BISHOP);
//    long rooks = board.getPieces(color, ROOK);
//    long queens = board.getPieces(color, QUEEN);
//    long king = board.getPieces(color, KING);
//
//    Color opponentColor = color.getOpponent();
//    long enemyPawns = board.getPieces(opponentColor, PAWN);
//    long enemyKnights = board.getPieces(opponentColor, KNIGHT);
//    long enemyBishops = board.getPieces(opponentColor, BISHOP);
//    long enemyRooks = board.getPieces(opponentColor, ROOK);
//    long enemyQueens = board.getPieces(opponentColor, QUEEN);
//    long enemyKing = board.getPieces(opponentColor, KING);
//
//    long friendly = pawns | knights | bishops | rooks | queens | king;
//    long enemy = enemyPawns | enemyKnights | enemyBishops | enemyRooks | enemyQueens | enemyKing;
//    long occupied = friendly | enemy;
//    long empty = ~occupied;
//    long unsafe = generateUnsafeSquares(
//        king, friendly, enemy,
//        enemyPawns, enemyKnights, enemyBishops,
//        enemyRooks, enemyQueens, enemyKing
//    );
//
//    long epFileMask = board.getEnPasssantFileMask();
//
//    List<CheckingPiece> checkingPieces = getCheckingPieces(
//        color, king, occupied,
//        enemyPawns, enemyKnights,
//        enemyBishops, enemyRooks, enemyQueens
//    );
//
//    // king is not in check, generate moves normally
//    if (checkingPieces.isEmpty()) {
//      generatePawnMoves(moves, color, pawns, friendly, enemy, universe);
//      generateEnPassantMoves(moves, color, pawns, enemyPawns, epFileMask, universe);
//      generateKnightMoves(moves, knights, friendly, enemy, universe);
//      generateBishopMoves(moves, bishops, friendly, enemy, universe);
//      generateRookMoves(moves, rooks, friendly, enemy, universe);
//      generateQueenMoves(moves, queens, friendly, enemy, universe);
//      generateKingMoves(moves, king, friendly, enemy, unsafe);
//      generateCastlingMoves(moves, color, king, rooks, occupied, unsafe, true, true); // TODO: put actual booleans later
//    }
//
//    // king is in check, use alternative move generation
//    else {
//      //
//      if (checkingPieces.size() == 1) {
//        CheckingPiece checkingPiece = checkingPieces.getFirst();
//        Type checkerType = checkingPiece.type;
//        int checkerSquare = checkingPiece.square;
//        long checker = 1L << checkerSquare;
//
//        // generate moves which block or capture the attacking piece
//        if (checkerType != KNIGHT && checkerType != PAWN) {
//          int kingSquare = Long.numberOfTrailingZeros(king);
//
//          // get line of sight between king and checking piece for blocking
//          long lineOfSight = 0L;
//          switch (checkerType) {
//            case BISHOP -> {
//              lineOfSight |= calculateDiagonalMoves(checker, occupied, checkerSquare);
//              lineOfSight &= calculateDiagonalMoves(king, occupied, kingSquare);
//            }
//            case ROOK -> {
//              lineOfSight |= calculateStraightMoves(checker, occupied, checkerSquare);
//              lineOfSight &= calculateStraightMoves(king, occupied, kingSquare);
//            }
//            case QUEEN -> {
//              lineOfSight |= calculateStraightMoves(checker, occupied, checkerSquare) | calculateDiagonalMoves(checker, occupied, checkerSquare);
//              lineOfSight &= calculateStraightMoves(king, occupied, kingSquare) | calculateDiagonalMoves(king, occupied, kingSquare);
//            }
//          }
//
//          // get mask for capturing the checking piece or blocking the attack
//          long captureOrBlock = checker | lineOfSight;
//
//          generatePawnMoves(moves, color, pawns, friendly, enemy, captureOrBlock);
//          generateEnPassantMoves(moves, color, pawns, enemyPawns, epFileMask, captureOrBlock);
//          generateKnightMoves(moves, knights, friendly, enemy, captureOrBlock);
//          generateBishopMoves(moves, bishops, friendly, enemy, captureOrBlock);
//          generateRookMoves(moves, rooks, friendly, enemy, captureOrBlock);
//          generateQueenMoves(moves, queens, friendly, enemy, captureOrBlock);        }
//
//        // generate moves which capture the attacking piece
//        else {
//          generatePawnMoves(moves, color, pawns, friendly, enemy, checker);
//          generateEnPassantMoves(moves, color, pawns, enemyPawns, epFileMask, checker);
//          generateKnightMoves(moves, knights, friendly, enemy, checker);
//          generateBishopMoves(moves, bishops, friendly, enemy, checker);
//          generateRookMoves(moves, rooks, friendly, enemy, checker);
//          generateQueenMoves(moves, queens, friendly, enemy, checker);
//        }
//      }
//
//      // generate king moves to safety
//      generateKingMoves(moves, king, friendly, enemy, unsafe);
//    }
//
//    return moves;
}
