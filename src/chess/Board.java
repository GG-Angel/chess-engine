package chess;


import static chess.Color.BLACK;
import static chess.Color.WHITE;
import static chess.Masks.fileA;
import static chess.MoveType.DOUBLE_PAWN_PUSH;

import java.util.Arrays;
import java.util.Stack;

public class Board {
  public static int toSquare(int rank, int file) {
    return rank * 8 + file;
  }

  // bitboards
  private final long[] bitboards;
  private long whitePieces, blackPieces;

  // progression trackers
  private final Stack<Move> previousMoves;

  // state booleans
  private boolean whiteToMove;
  private boolean castleWK, castleWQ, castleBK, castleBQ;

  public Board() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public Board(String fen) {
    this.bitboards = new long[Piece.values().length];
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
        Piece type = Piece.fromChar(symbol);
        int square = toSquare(rank, file);
        bitboards[type.index()] |= (1L << square);
        file++;
      }
    }
    whitePieces = refreshColorPieces(WHITE);
    blackPieces = refreshColorPieces(BLACK);

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

  public long getPiecesByType(Piece type) {
    return bitboards[type.index()];
  }

  public long getPiecesByColor(Color color) {
    return color == WHITE ? whitePieces : blackPieces;
  }

  public long getOccupied() {
    return whitePieces | blackPieces;
  }

  public long getEmpty() {
    return ~getOccupied();
  }

  private long refreshColorPieces(Color color) {
    long pieces = 0L;
    for (int i = 0; i < 6; i++) {
      pieces |= bitboards[color.startIndex() + i];
    }
    return pieces;
  }

  public long getEnPasssantFileMask() {
    Move lastMove = previousMoves.peek();

    if (lastMove == null || lastMove.type() != DOUBLE_PAWN_PUSH) {
      return 0; // en passant not possible
    }

    // return a mask of the file that the pawn resides on
    return fileA << (lastMove.to() % 8);
  }

  private Piece[] getTypedBoard() {
    Piece[] board = new Piece[64];
    for (Piece pieceType : Piece.values()) {
      long bitboard = getPiecesByType(pieceType);
      while (bitboard != 0) {
        int square = Long.numberOfTrailingZeros(bitboard); // get LSB index
        board[square] = pieceType;
        bitboard &= bitboard - 1; // remove LSB
      }
    }
    return board;
  }

  public static String bitboardToString(long bitboard) {
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
    return sb.toString();
  }

  @Override
  public String toString() {
    Piece[] board = getTypedBoard();
    StringBuilder sb = new StringBuilder();
    for (int rank = 7; rank >= 0; rank--) {
      sb.append((rank + 1)).append(" | ");
      for (int file = 0; file < 8; file++) {
        int square = toSquare(rank, file);
        Piece squarePiece = board[square];
        if (squarePiece != null) {
          sb.append(squarePiece.toChar());
        } else {
          sb.append("."); // empty space
        }

        if (file < 7) sb.append(" ");
      }
      sb.append('\n');
    }
    sb.append("   ————————————————\n").append("    A B C D E F G H");
    return sb.toString();
  }
}
