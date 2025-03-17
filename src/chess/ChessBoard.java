package chess;

import static chess.BitBoard.hasPieceAtBit;
import static chess.BitBoard.setBit;
import static chess.Piece.isWhite;
import static chess.PieceLookup.getPieceFromSymbol;
import static chess.PieceLookup.getTypeSymbol;
import static chess.Piece.NUM_OF_PIECE_BITBOARDS;
import static chess.Piece.getAllPieceTypes;
import static chess.Utilities.toSquarePosition;

import java.util.ArrayList;
import java.util.List;

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
        Piece pieceType = getPieceFromSymbol(symbol);
        long bitboard = getBitboardPiece(pieceType);
        int square = toSquarePosition(rank, file);
        bitboardPieces[pieceType.ordinal()] = setBit(bitboard, square);
        file++;
      }
    }
  }

  public List<Move> generatePawnMoves(Piece piece, int square) {
    List<Move> moves = new ArrayList<>();

    boolean isWhite = isWhite(piece);
    int direction = isWhite ? 8 : -8;
    int homeRank = isWhite ? 1 : 6;
    int promotionRank = isWhite ? 7 : 0;

    // single pawn push
    int targetSquare = square + direction;
    if (isSquareEmpty(targetSquare)) {
      moves.add(new Move(square, targetSquare));

      
    }
  }

  public boolean isSquareEmpty(int square) {
    return !hasPieceAtBit(getBitboardOccupied(), square);
  }

  public long getBitboardPiece(Piece piece) {
    return bitboardPieces[piece.ordinal()];
  }

  public long getBitboardColor(boolean white) {
    int startIndex = white ? 0 : 6;
    long bitboard = 0L;
    for (int i = startIndex; i < startIndex + 6; i++) {
      bitboard |= bitboardPieces[i];
    }
    return bitboard;
  }

  public long getBitboardOccupied() {
    long bitboard = 0L;
    for (int i = 0; i < NUM_OF_PIECE_BITBOARDS; i++) {
      bitboard |= bitboardPieces[i];
    }
    return bitboard;
  }

  public long getBitboardEmpty() {
    return ~getBitboardOccupied();
  }

  public Piece[] getBoard() {
    Piece[] board = new Piece[64];
    for (Piece pieceType : getAllPieceTypes()) {
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
    Piece[] board = getBoard();
    StringBuilder sb = new StringBuilder();
    for (int rank = 0; rank < 8; rank++) {
      for (int file = 0; file < 8; file++) {
        int square = toSquarePosition(rank, file);
        Piece pieceAtSquare = board[square];
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
    ChessBoard board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(board);

//    ChessGUI view = new ChessGUI(board);
//    view.setVisible(true);
  }
}
