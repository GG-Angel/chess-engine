package chess;

import static chess.Board.printBitboard;
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
import static chess.Piece.Color.BLACK;
import static chess.Piece.Color.WHITE;
import static chess.Piece.Type.BISHOP;
import static chess.Piece.Type.KING;
import static chess.Piece.Type.KNIGHT;
import static chess.Piece.Type.PAWN;
import static chess.Piece.Type.QUEEN;
import static chess.Piece.Type.ROOK;
import static java.lang.Long.reverse;

import chess.Piece.Color;
import chess.Piece.Type;
import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
  public static void generatePawnMoves(List<Move> moves, Color color, long pawns, long friendly, long enemy) {
    long empty = ~(friendly | enemy);
    long promotionRankMask = (color == WHITE) ? rank8 : rank1;
    long doublePushMask = (color == WHITE) ? rank4 : rank5;
    int direction = (color == WHITE) ? 1 : -1;
    int shift = 8 * direction;

    long quietMoves = calculatePawnQuietMoves(pawns, empty, promotionRankMask, shift);
    long doublePushMoves = calculatePawnDoublePushMoves(pawns, empty, doublePushMask, shift);
    long leftCaptureMoves = calculatePawnLeftCaptures(pawns, enemy, promotionRankMask, direction);
    long rightCaptureMoves = calculatePawnRightCaptures(pawns, enemy, promotionRankMask, direction);
    long quietPromotionMoves = calculatePawnQuietPromotions(pawns, empty, promotionRankMask, shift);
    long leftCapturePromotionMoves = calculatePawnLeftPromotionCaptures(pawns, enemy, promotionRankMask, direction);
    long rightCapturePromotionMoves = calculatePawnRightPromotionCaptures(pawns, enemy, promotionRankMask, direction);

    addPawnMoves(moves, quietMoves, shift, QUIET);
    addPawnMoves(moves, doublePushMoves, shift * 2, DOUBLE_PAWN_PUSH);
    addPawnMoves(moves, leftCaptureMoves, 7 * direction, CAPTURE);
    addPawnMoves(moves, rightCaptureMoves, 9 * direction, CAPTURE);
    addPromotionMoves(moves, quietPromotionMoves, shift, Promotions);
    addPromotionMoves(moves, leftCapturePromotionMoves, 7 * direction, PromotionCaptures);
    addPromotionMoves(moves, rightCapturePromotionMoves, 9 * direction, PromotionCaptures);
  }

  private static long calculatePawnQuietMoves(long pawns, long empty, long promotionRankMask, int shift) {
    return shiftPawns(pawns, shift) & empty & ~promotionRankMask;
  }

  private static long calculatePawnDoublePushMoves(long pawns, long empty, long doublePushMask, int shift) {
    long doublePawnPushTargets = empty & shiftPawns(empty, shift / 2);
    return shiftPawns(pawns, shift) & doublePawnPushTargets & doublePushMask;
  }

  private static long calculatePawnLeftCaptures(long pawns, long enemy, long promotionRankMask, int direction) {
    return shiftPawns(pawns, 7 * direction) & enemy & ~(promotionRankMask | fileA);
  }

  private static long calculatePawnRightCaptures(long pawns, long enemy, long promotionRankMask, int direction) {
    return shiftPawns(pawns, 9 * direction) & enemy & ~(promotionRankMask | fileH);
  }

  private static long calculatePawnQuietPromotions(long pawns, long empty, long promotionRankMask, int shift) {
    return shiftPawns(pawns, shift) & empty & promotionRankMask;
  }

  private static long calculatePawnLeftPromotionCaptures(long pawns, long enemy, long promotionRankMask, int direction) {
    return shiftPawns(pawns, 7 * direction) & enemy & (promotionRankMask & ~fileA);
  }

  private static long calculatePawnRightPromotionCaptures(long pawns, long enemy, long promotionRankMask, int direction) {
    return shiftPawns(pawns, 9 * direction) & enemy & (promotionRankMask & ~fileH);
  }

  private static void addPawnMoves(List<Move> moves, long pawnMoves, int shift, MoveType moveType) {
    while (pawnMoves != 0) {
      int to = Long.numberOfTrailingZeros(pawnMoves);
      int from = to - shift;
      moves.add(new Move(from, to, moveType));
      pawnMoves &= pawnMoves - 1;
    }
  }

  private static void addPromotionMoves(List<Move> moves, long promotionMoves, int shift, MoveType[] promotionTypes) {
    while (promotionMoves != 0) {
      int to = Long.numberOfTrailingZeros(promotionMoves);
      int from = to - shift;
      for (MoveType promotion : promotionTypes) {
        moves.add(new Move(from, to, promotion));
      }
      promotionMoves &= promotionMoves - 1;
    }
  }

  // Utility function to shift pawn bitboards
  private static long shiftPawns(long bitboard, int amount) {
    return amount >= 0 ? (bitboard << amount) : (bitboard >> -amount);
  }

  public static void generateEnPassantMoves(
      List<Move> moves, Color color,
      long friendlyPawns, long enemyPawns, long epFileMask
  ) throws IllegalArgumentException {
    if (epFileMask == 0) return;

    int direction = (color == WHITE) ? 8 : -8;
    long enemyDoublePushRank = (color == WHITE) ? rank5 : rank4;
    long leftOverflowMask = (color == WHITE) ? fileA : fileH;
    long rightOverflowMask = (color == WHITE) ? fileH : fileA;

    long epPawnLeft = ((friendlyPawns >> 1) & enemyPawns) & enemyDoublePushRank & ~leftOverflowMask & epFileMask;
    long epPawnRight = ((friendlyPawns << 1) & enemyPawns) & enemyDoublePushRank & ~rightOverflowMask & epFileMask;

    if (epPawnLeft == 0 && epPawnRight == 0) {
      throw new IllegalArgumentException("Failed to find en passant pawn, likely due to invalid mask.");
    }

    long epPawn = epPawnLeft != 0 ? epPawnLeft : epPawnRight;
    int epPawnSquare = Long.numberOfTrailingZeros(epPawn);

    int epCapturePawnSquare = epPawnSquare + (epPawnLeft != 0 ? 1 : -1);
    int epCaptureTargetSquare = epPawnSquare + direction;

    moves.add(new Move(epCapturePawnSquare, epCaptureTargetSquare, EP_CAPTURE));
  }

  private static long calculateKnightMoves(int square) {
    long knightMoves = knightMask;

    // apply move mask to knight position
    if (square > 18) {
      knightMoves <<= (square - 18);
    } else {
      knightMoves >>= (18 - square);
    }

    // ensure moves do not wrap around the board
    knightMoves &= (square % 8 < 4) ? ~fileGH : ~fileAB;

    return knightMoves;
  }

  public static void generateKnightMoves(List<Move> moves, long knights, long friendly, long enemy) {
    while (knights != 0) {
      int knightSquare = Long.numberOfTrailingZeros(knights);
      long knightMoves = calculateKnightMoves(knightSquare);

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

      long straightMoves = calculateStraightMoves(rook, occupied, rookSquare);
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

      long diagonalMoves = calculateDiagonalMoves(bishop, occupied, bishopSquare);
      long bishopMoves = diagonalMoves & ~friendly;

      createMovesFromBitboard(moves, bishopMoves, bishopSquare, enemy);
      bishops &= bishops - 1;
    }
  }

  private static long calculateStraightMoves(long piece, long occupied, int square) {
    long rankMask = rankMasks[square];
    long fileMask = fileMasks[square];

    long rankMoves = calculateSlidingMoves(piece, occupied, rankMask);
    long fileMoves = calculateSlidingMoves(piece, occupied, fileMask);

    return rankMoves | fileMoves;
  }

  private static long calculateDiagonalMoves(long piece, long occupied, int square) {
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
      int target = Long.numberOfTrailingZeros(movesBitboard);
      MoveType moveType = (enemy & (1L << target)) != 0 ? CAPTURE : QUIET;
      moves.add(new Move(pieceSquare, target, moveType));
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
      long knightMoves = calculateKnightMoves(knightSquare);

      unsafe |= knightMoves;
      enemyKnights &= enemyKnights - 1;
    }

    // diagonally sliding pieces
    long enemyDiagonalPieces = enemyBishops | enemyQueens;
    while (enemyDiagonalPieces != 0) {
      int pieceSquare = Long.numberOfTrailingZeros(enemyDiagonalPieces);
      long piece = 1L << pieceSquare;
      long diagonalMoves = calculateDiagonalMoves(piece, occupiedWithoutKing, pieceSquare);

      unsafe |= diagonalMoves;
      enemyDiagonalPieces &= enemyDiagonalPieces - 1;
    }

    // straight sliding pieces
    long enemyStraightPieces = enemyRooks | enemyQueens;
    while (enemyStraightPieces != 0) {
      int pieceSquare = Long.numberOfTrailingZeros(enemyStraightPieces);
      long piece = 1L << pieceSquare;
      long straightMoves = calculateStraightMoves(piece, occupiedWithoutKing, pieceSquare);

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

  public record CheckingPiece(int square, Type type) {
    @Override
    public String toString() {
      char file = (char) ('a' + (square % 8));
      int rank = square / 8 + 1;
      return String.format("(%c%d, %s)", file, rank, type);
    }
  }

  public static List<CheckingPiece> getCheckingPieces(
      Color color, long king, long occupied,
      long enemyPawns, long enemyKnights,
      long enemyBishops, long enemyRooks, long enemyQueens
  ) {
    List<CheckingPiece> checkingPieces = new ArrayList<>();

    long checkingPawns = 0L;
    int pawnDirection = color == WHITE ? 1 : -1;
    if ((color == WHITE && (king & rank8) == 0) || (color == BLACK && (king & rank1) == 0)) {
      checkingPawns |= shiftPawns(king, 7 * pawnDirection) & ~fileA;
      checkingPawns |= shiftPawns(king, 9 * pawnDirection) & ~fileH;
    } // TODO: might be buggy

    int kingSquare = Long.numberOfTrailingZeros(king);
    long diagonals = calculateDiagonalMoves(king, occupied, kingSquare) & (enemyBishops | enemyQueens);
    long straights = calculateStraightMoves(king, occupied, kingSquare) & (enemyRooks | enemyQueens);

    checkingPawns &= enemyPawns;
    long checkingKnights = calculateKnightMoves(kingSquare) & enemyKnights;
    long checkingBishops = diagonals & enemyBishops;
    long checkingRooks = straights & enemyRooks;
    long checkingQueens = (diagonals | straights) & enemyQueens;

    addCheckingPiece(checkingPieces, checkingPawns, PAWN);
    addCheckingPiece(checkingPieces, checkingKnights, KNIGHT);
    addCheckingPiece(checkingPieces, checkingBishops, BISHOP);
    addCheckingPiece(checkingPieces, checkingRooks, ROOK);
    addCheckingPiece(checkingPieces, checkingQueens, QUEEN);

    return checkingPieces;
  }

  private static void addCheckingPiece(List<CheckingPiece> checkingPieces, long piece, Type type) {
    while (piece != 0) {
      int square = Long.numberOfTrailingZeros(piece);
      checkingPieces.add(new CheckingPiece(square, type));
      piece &= piece - 1;
    }
  }

  private static void generateCheckEvasions(
      List<Move> moves, Color color, long evasion, long occupied, long empty, long enemy,
      long pawns, long knights, long bishops, long rooks, long queens
  ) {
    long promotionRankMask = (color == WHITE) ? rank8 : rank1;
    long doublePushMask = (color == WHITE) ? rank4 : rank5;
    int direction = (color == WHITE) ? 1 : -1;
    int shift = 8 * direction;

    long quietMoves = calculatePawnQuietMoves(pawns, empty, promotionRankMask, shift) & evasion;
    long doublePushMoves = calculatePawnDoublePushMoves(pawns, empty, doublePushMask, shift) & evasion;
    long leftCaptureMoves = calculatePawnLeftCaptures(pawns, enemy, promotionRankMask, direction) & evasion;
    long rightCaptureMoves = calculatePawnRightCaptures(pawns, enemy, promotionRankMask, direction) & evasion;
    long quietPromotionMoves = calculatePawnQuietPromotions(pawns, empty, promotionRankMask, shift) & evasion;
    long leftCapturePromotionMoves = calculatePawnLeftPromotionCaptures(pawns, enemy, promotionRankMask, direction) & evasion;
    long rightCapturePromotionMoves = calculatePawnRightPromotionCaptures(pawns, enemy, promotionRankMask, direction) & evasion;

    addPawnMoves(moves, quietMoves, shift, QUIET);
    addPawnMoves(moves, doublePushMoves, shift * 2, DOUBLE_PAWN_PUSH);
    addPawnMoves(moves, leftCaptureMoves, 7 * direction, CAPTURE);
    addPawnMoves(moves, rightCaptureMoves, 9 * direction, CAPTURE);
    addPromotionMoves(moves, quietPromotionMoves, shift, Promotions);
    addPromotionMoves(moves, leftCapturePromotionMoves, 7 * direction, PromotionCaptures);
    addPromotionMoves(moves, rightCapturePromotionMoves, 9 * direction, PromotionCaptures);

    while (knights != 0) {
      int knightSquare = Long.numberOfTrailingZeros(knights);
      long knightMoves = calculateKnightMoves(knightSquare) & evasion;
      createMovesFromBitboard(moves, knightMoves, knightSquare, enemy);
      knights &= knights - 1;
    }

    long diagonalPieces = bishops | queens;
    while (diagonalPieces != 0) {
      int diagonalSquare = Long.numberOfTrailingZeros(diagonalPieces);
      long diagonalPiece = 1L << diagonalSquare;
      long diagonalMoves = calculateDiagonalMoves(diagonalPiece, occupied, diagonalSquare) & evasion;
      createMovesFromBitboard(moves, diagonalMoves, diagonalSquare, enemy);
      diagonalPieces &= diagonalPieces - 1;
    }

    long straightPieces = rooks | queens;
    while (straightPieces != 0) {
      int straightSquare = Long.numberOfTrailingZeros(straightPieces);
      long straightPiece = 1L << straightSquare;
      long straightMoves = calculateStraightMoves(straightPiece, occupied, straightSquare) & evasion;
      createMovesFromBitboard(moves, straightMoves, straightSquare, enemy);
      straightPieces &= straightPieces - 1;
    }
  }

  public static List<Move> generatePseudoLegalMoves(Board board, Color color) {
    List<Move> moves = new ArrayList<>();

    long pawns = board.getPieces(color, PAWN);
    long knights = board.getPieces(color, KNIGHT);
    long bishops = board.getPieces(color, BISHOP);
    long rooks = board.getPieces(color, ROOK);
    long queens = board.getPieces(color, QUEEN);
    long king = board.getPieces(color, KING);

    Color opponentColor = color.getOpponent();
    long enemyPawns = board.getPieces(opponentColor, PAWN);
    long enemyKnights = board.getPieces(opponentColor, KNIGHT);
    long enemyBishops = board.getPieces(opponentColor, BISHOP);
    long enemyRooks = board.getPieces(opponentColor, ROOK);
    long enemyQueens = board.getPieces(opponentColor, QUEEN);
    long enemyKing = board.getPieces(opponentColor, KING);

    long friendly = pawns | knights | bishops | rooks | queens | king;
    long enemy = enemyPawns | enemyKnights | enemyBishops | enemyRooks | enemyQueens | enemyKing;
    long occupied = friendly | enemy;
    long empty = ~occupied;
    long unsafe = generateUnsafeSquares(
        king, friendly, enemy,
        enemyPawns, enemyKnights, enemyBishops,
        enemyRooks, enemyQueens, enemyKing
    );

    List<CheckingPiece> checkingPieces = getCheckingPieces(
        color, king, occupied,
        enemyPawns, enemyKnights,
        enemyBishops, enemyRooks, enemyQueens
    );

    // king is not in check, generate moves normally
    if (checkingPieces.isEmpty()) {
      generatePawnMoves(moves, color, pawns, friendly, enemy);
      generateKnightMoves(moves, knights, friendly, enemy);
      generateBishopMoves(moves, bishops, friendly, enemy);
      generateRookMoves(moves, rooks, friendly, enemy);
      generateQueenMoves(moves, queens, friendly, enemy);
      generateKingMoves(moves, king, friendly, enemy, unsafe);
    }

    // king is in check, use alternative move generation
    else {
      //
      if (checkingPieces.size() == 1) {
        CheckingPiece checkingPiece = checkingPieces.getFirst();
        Type checkerType = checkingPiece.type;
        int checkerSquare = checkingPiece.square;
        long checker = 1L << checkerSquare;

        // generate moves which block or capture the attacking piece
        if (checkerType != KNIGHT && checkerType != PAWN) {
          int kingSquare = Long.numberOfTrailingZeros(king);

          // get line of sight between king and checking piece for blocking
          long lineOfSight = 0L;
          switch (checkerType) {
            case BISHOP -> {
              lineOfSight |= calculateDiagonalMoves(checker, occupied, checkerSquare);
              lineOfSight &= calculateDiagonalMoves(king, occupied, kingSquare);
            }
            case ROOK -> {
              lineOfSight |= calculateStraightMoves(checker, occupied, checkerSquare);
              lineOfSight &= calculateStraightMoves(king, occupied, kingSquare);
            }
            case QUEEN -> {
              lineOfSight |= calculateStraightMoves(checker, occupied, checkerSquare) | calculateDiagonalMoves(checker, occupied, checkerSquare);
              lineOfSight &= calculateStraightMoves(king, occupied, kingSquare) | calculateDiagonalMoves(king, occupied, kingSquare);
            }
          }

          long captureOrBlock = checker | lineOfSight;
          generateCheckEvasions(moves, color, captureOrBlock, occupied, empty, enemy, pawns, knights, bishops, rooks, queens);
        }

        // generate moves which capture the attacking piece
        else {
          generateCheckEvasions(moves, color, checker, occupied, empty, enemy, pawns, knights, bishops, rooks, queens);
        }
      }

      // generate king moves to safety
      generateKingMoves(moves, king, friendly, enemy, unsafe);
    }

    return moves;
  }
}
