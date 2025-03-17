package chess;

import static chess.Utilities.toSquarePosition;

public class Board {
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
        long bitboard = getPieces(type);

//        Piece type = getPieceFromSymbol(symbol);
//        long bitboard = getBitboardPiece(pieceType);
//        int square = toSquarePosition(rank, file);
//        bitboardPieces[pieceType.ordinal()] = setBit(bitboard, square);
        file++;
      }
    }
  }

  private long getPieces(Piece type) {
    return bitboards[type.index()];
  }
}
