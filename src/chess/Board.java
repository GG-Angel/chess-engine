package chess;

public class Board {
  private long[] bitboards;

  public Board() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public Board(String fen) {
    this.bitboards = new long[12];
    loadPosition(fen);
  }

  private void loadPosition(String fen) {

  }
}
