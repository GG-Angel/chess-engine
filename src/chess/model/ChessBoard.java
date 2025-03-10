package chess.model;

public class ChessBoard implements Board {
  public static int BOARD_SIZE = 8;

  private final Piece[] board;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.board = new Piece[64];

    initializeBoardFromFen(fen);
  }

  private void initializeBoardFromFen(String fen) {

  }

  @Override
  public Piece getPieceAtPosition(int x, int y) {
    return this.board[(x * 8) + y];
  }
}