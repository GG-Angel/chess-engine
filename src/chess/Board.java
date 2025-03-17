package chess;


public class Board {
  public static int toSquare(int rank, int file) {
    return rank * 8 + file;
  }

  private final long[] bitboards;

  public Board() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public Board(String fen) {
    this.bitboards = new long[Piece.values().length];
    loadPosition(fen);
  }

  public void loadPosition(String fen) {
    // reset bitboards
    for (int i = 0; i < 12; i++) {
      bitboards[i] = 0L;
    }

    String[] fenParams = fen.split(" ");

    // set pieces on their corresponding bitboards
    char[] fenBoard = fenParams[0].toCharArray();
    int rank = 0, file = 0;
    for (char symbol : fenBoard) {
      if (symbol == '/') {
        file = 0;
        rank++;
      } else if (Character.isDigit(symbol)) {
        file += Character.getNumericValue(symbol);
      } else {
        Piece type = Piece.fromChar(symbol);
        long bitboard = getPiecesByType(type);
        int square = toSquare(rank, file);
        bitboards[type.index()] = bitboard | (1L << square);
        file++;
      }
    }
  }

  private long getPiecesByType(Piece type) {
    return bitboards[type.index()];
  }

  private long getPiecesByColor(Color color) {
    long pieces = 0L;
    for (int i = 0; i < 6; i++) {
      pieces |= bitboards[color.startIndex() + i];
    }
    return pieces;
  }

  private long getOccupied() {
    long occupied = 0L;
    for (long bitboard : bitboards) {
      occupied |= bitboard;
    }
    return occupied;
  }

  private long getEmpty() {
    return ~getOccupied();
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

  @Override
  public String toString() {
    Piece[] board = getTypedBoard();
    StringBuilder sb = new StringBuilder();
    for (int rank = 0; rank < 8; rank++) {
      for (int file = 0; file < 8; file++) {
        int square = toSquare(rank, file);
        Piece squarePiece = board[square];
        if (squarePiece != null) {
          sb.append(squarePiece.toChar());
        } else {
          sb.append("."); // empty space
        }

        if (file < 7) {
          sb.append(" ");
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
