package chess;

public class Board {
  public static int getSquare(int rank, int file) {
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
        int square = getSquare(rank, file);
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

  @Override
  public String toString() {
    return "";
  }
}
