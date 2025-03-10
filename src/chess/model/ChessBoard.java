package chess.model;

import static chess.model.PieceColor.*;

public class ChessBoard implements Board {
  public static int BOARD_SIZE = 8;
  public static int to1D(int x, int y) {
    return y * 8 + x;
  }

  private final Piece[] board;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.board = new Piece[64];

    initializeBoardFromFen(fen);
  }

  private void initializeBoardFromFen(String fen) {
    String[] fenParams = fen.split(" ");

    int x = 0, y = 0;
    char[] fenBoard = fenParams[0].toCharArray();
    for (char symbol : fenBoard) {
      if (symbol == '/') {
        x = 0;
        y++;
      } else if (Character.isDigit(symbol)) {
        x += Character.getNumericValue(symbol);
      } else {
        PieceColor color = Character.isUpperCase(symbol) ? WHITE : BLACK;
        PieceType type = PieceLookup.getType(symbol);
        Piece piece = PieceFactory.createPiece(color, type, x, y);
        this.board[to1D(x, y)] = piece;
        x++;
      }
    }
  }

  @Override
  public Piece getPieceAtPosition(int x, int y) {
    return getPieceAtPosition(to1D(x, y));
  }

  @Override
  public Piece getPieceAtPosition(int position) {
    return this.board[position];
  }
}