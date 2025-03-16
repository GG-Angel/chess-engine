package chess;

import static chess.PieceLookup.getPieceFromSymbol;
import static chess.PieceLookup.getTypeSymbolFromPiece;
import static chess.PieceType.NUM_OF_PIECE_BITBOARDS;
import static chess.PieceType.getAllPieceTypes;
import static chess.Utilities.toPosition;

import java.util.Arrays;

public class ChessBoard {
  private final long[] pieceBitBoards;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.pieceBitBoards = new long[NUM_OF_PIECE_BITBOARDS];

    loadPosition(fen);
  }

  public void loadPosition(String fen) {
    // reset bitboards
    for (int i = 0; i < NUM_OF_PIECE_BITBOARDS; i++) {
      pieceBitBoards[i] = 0L;
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
        long bitboard = getBitBoard(pieceType);
        int square = toPosition(rank, file);
        pieceBitBoards[pieceType.ordinal()] = BitBoard.setBit(bitboard, square);
        file++;
      }
    }
  }

  public long getBitBoard(PieceType pieceType) {
    return pieceBitBoards[pieceType.ordinal()];
  }

  @Override
  public String toString() {
    char[] fullBoard = new char[64];
    Arrays.fill(fullBoard, '.');

    for (PieceType type : getAllPieceTypes()) {
      long board = pieceBitBoards[type.ordinal()];
      while (board != 0) {
        int square = Long.numberOfTrailingZeros(board); // get least significant bit index
        fullBoard[square] = getTypeSymbolFromPiece(type);
        board &= board - 1; // remove LSB
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int rank = 0; rank < 8; rank++) {
      for (int file = 0; file < 8; file++) {
        sb.append(fullBoard[rank * 8 + file]).append(" ");
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
