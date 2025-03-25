package chess;

import static chess.Masks.fileA;
import static chess.MoveType.DOUBLE_PAWN_PUSH;

import chess.Piece.Color;
import chess.Piece.Type;
import java.util.Arrays;
import java.util.Stack;

public class Board {
  // bitboards
  private final long[] bitboards;

  // progression trackers
  private final Stack<Move> previousMoves;

  // state booleans
  private boolean whiteToMove;
  private boolean castleWK, castleWQ, castleBK, castleBQ;

  public Board() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public Board(String fen) {
    this.bitboards = new long[Type.values().length * Color.values().length];
    this.previousMoves = new Stack<>();
    loadPosition(fen);
  }

  public void loadPosition(String fen) {
    // reset bitboards and castling privileges
    Arrays.fill(bitboards, 0L);
    castleWK = castleWQ = castleBK = castleBQ = false;

    // parse fen parameters
    String[] fenParams = fen.split(" ");

    // set pieces on their corresponding bitboards
    char[] fenBoard = fenParams[0].toCharArray();
    int rank = 7, file = 0;
    for (char symbol : fenBoard) {
      if (symbol == '/') {
        file = 0;
        rank--;
      } else if (Character.isDigit(symbol)) {
        file += Character.getNumericValue(symbol);
      } else {
        Color pieceColor = Color.fromChar(symbol);
        Type pieceType = Type.fromChar(symbol);
        int square = toSquare(rank, file);
        bitboards[Piece.getIndex(pieceColor, pieceType)] |= (1L << square);
        file++;
      }
    }

    // set color to move
    whiteToMove = fenParams[1].equals("w");

    // set castling privileges
    char[] fenCastling = fenParams[2].toCharArray();
    for (char symbol : fenCastling) {
      switch (symbol) {
        case 'K' -> castleWK = true;
        case 'Q' -> castleWQ = true;
        case 'k' -> castleBK = true;
        case 'q' -> castleBQ = true;
      }
    }

    // log previous en passant move (if present)
    String enPassantTarget = fenParams[3];
    if (!enPassantTarget.equals("-")) {
      int epFile = enPassantTarget.charAt(0) - 'a';
      int epRank = enPassantTarget.charAt(1) - '1';
      int epSquare = toSquare(epRank, epFile); // has the square behind the pawn (indexed at 0)
      switch (epRank) {
        case 2 -> previousMoves.push(new Move(epSquare - 8, epSquare + 8, DOUBLE_PAWN_PUSH)); // white
        case 5 -> previousMoves.push(new Move(epSquare + 8, epSquare - 8, DOUBLE_PAWN_PUSH)); // black
        default -> throw new IllegalArgumentException("Invalid en passant target.");
      }
    }
  }

  public long getPieces(Color color, Type type) {
    return bitboards[Piece.getIndex(color, type)];
  }

  public long getPiecesByColor(Color color) {
    long pieces = 0L;
    for (int i = color.offset; i < color.offset + 6; i++) {
      pieces |= bitboards[i];
    }
    return pieces;
  }

  public long getOccupied() {
    return getPiecesByColor(Color.WHITE) | getPiecesByColor(Color.BLACK);
  }

  public long getEmpty() {
    return ~getOccupied();
  }

  public long getEnPasssantFileMask() {
    if (previousMoves.isEmpty()) {
      return 0; // there is no previous en passant
    }

    Move lastMove = previousMoves.peek();

    if (lastMove == null || lastMove.type() != DOUBLE_PAWN_PUSH) {
      return 0; // en passant not possible
    }

    // return a mask of the file that the pawn resides on
    return fileA << (lastMove.to() % 8);
  }

  private static int toSquare(int rank, int file) {
    return rank * 8 + file;
  }

  public static void printBitboard(long bitboard) {
    StringBuilder sb = new StringBuilder();
    for (int rank = 7; rank >= 0; rank--) {
      for (int file = 0; file < 8; file++) {
        int square = toSquare(rank, file);
        long mask = (1L << square);
        if ((bitboard & mask) != 0) {
          sb.append('1');
        } else {
          sb.append('-');
        }

        if (file < 7) sb.append(" ");
      }
      sb.append('\n');
    }
    System.out.println(sb);
  }

  @Override
  public String toString() {
    char[] board = new char[64];
    Arrays.fill(board, '.');

    for (Color color : Color.values()) {
      for (Type type : Type.values()) {
        long piece = getPieces(color, type);
        while (piece != 0) {
          int pieceSquare = Long.numberOfTrailingZeros(piece);
          board[pieceSquare] = Piece.getSymbol(color, type);
          piece &= piece - 1;
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int rank = 7; rank >= 0; rank--) {
      sb.append(rank + 1).append(" | ");
      for (int file = 0; file < 8; file++) {
        int square = toSquare(rank, file);
        sb.append(board[square]);
        if (file < 7) sb.append(" ");
      }
      sb.append('\n');
    }
    sb.append("   ————————————————\n").append("    A B C D E F G H");

    return sb.toString();
  }
}
