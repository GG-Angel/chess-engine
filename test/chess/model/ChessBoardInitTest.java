package chess.model;

import static org.junit.jupiter.api.Assertions.*;

import chess.view.ChessTextView;
import chess.view.View;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class ChessBoardInitTest {

  @Test
  void testInitializeDefaultBoard() throws IOException {
    Board board = new ChessBoard();
    View view = new ChessTextView(board);
    view.renderBoard();
  }

  @Test
  void testInitializeKiwipeteBoard() throws IOException {
    Board board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    View view = new ChessTextView(board);
    view.renderBoard();
  }

  @Test
  void testInitializeOtherBoard() throws IOException {
    Board board = new ChessBoard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
    View view = new ChessTextView(board);
    view.renderBoard();
  }
}