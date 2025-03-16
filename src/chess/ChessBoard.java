package chess;

import static chess.PieceLookup.getPieceFromSymbol;
import static chess.PieceLookup.getTypeSymbol;
import static chess.PieceType.NUM_OF_PIECE_BITBOARDS;
import static chess.PieceType.getAllPieceTypes;
import static chess.Utilities.toSquarePosition;

public class ChessBoard {
  private final long[] bitboardPieces;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.bitboardPieces = new long[NUM_OF_PIECE_BITBOARDS];

    loadPosition(fen);
  }

  public void loadPosition(String fen) {
    // reset bitboards
    for (int i = 0; i < NUM_OF_PIECE_BITBOARDS; i++) {
      bitboardPieces[i] = 0L;
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
        PieceType pieceType = getPieceFromSymbol(symbol);
        long bitboard = getBitboardPiece(pieceType);
        int square = toSquarePosition(rank, file);
        bitboardPieces[pieceType.ordinal()] = BitBoard.setBit(bitboard, square);
        file++;
      }
    }
  }

  public long getBitboardColorPieces(boolean white) {
    int startIndex = white ? 0 : 6;
    int endIndex = white ? 6 : 0;

    long bitboardPiece = 0L;
    for (int i = startIndex; i < endIndex; i++) {
      bitboardPiece |= bitboardPieces[i];
    }
    return bitboardPiece;
  }



  public long getBitboardPiece(PieceType pieceType) {
    return bitboardPieces[pieceType.ordinal()];
  }

  public PieceType[] getBoard() {
    PieceType[] board = new PieceType[64];
    for (PieceType pieceType : getAllPieceTypes()) {
      long bitboard = getBitboardPiece(pieceType);
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
    PieceType[] board = getBoard();
    StringBuilder sb = new StringBuilder();
    for (int rank = 0; rank < 8; rank++) {
      for (int file = 0; file < 8; file++) {
        int square = toSquarePosition(rank, file);
        PieceType pieceAtSquare = board[square];
        sb.append(pieceAtSquare != null ? getTypeSymbol(pieceAtSquare) : ".");

        if (file < 7) {
          sb.append(" ");
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    ChessBoard board = new ChessBoard();
    System.out.println(board);
  }
}
