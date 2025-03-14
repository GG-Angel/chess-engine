package chess.model;

import chess.model.piece.PieceColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// test boards from https://www.chessprogramming.org/Perft_Results

class ChessBoardPerformanceTest {
  private long runPerft(Board board, int depth, PieceColor startingColor) {
    long start = System.nanoTime();
    long actualNodes = board.legalMovesPerft(depth, startingColor);
    long end = System.nanoTime();
    System.out.println(depth + ": " + ((end - start) / 1000000.0) + " ms");
    return actualNodes;
  }

  @ParameterizedTest
  @CsvSource({
      "0, 1",
      "1, 20",
//      "2, 400",
//      "3, 8902",
//    "4, 197281",
//    "5, 4865609",
  })
  void testInitialPosition(int depth, long expectedNodes) {
    Board board = new ChessBoard();
    long actualNodes = runPerft(board, depth, PieceColor.WHITE);
    Assertions.assertEquals(expectedNodes, actualNodes);
  }

  @ParameterizedTest
  @CsvSource ({
      "1, 48",
//      "2, 2039",
//      "3, 97862",
//    "4, 4085603",
//    "5, 193690690"
  })
  void testPositionTwo(int depth, long expectedNodes) {
    Board board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    long actualNodes = runPerft(board, depth, PieceColor.WHITE);
    Assertions.assertEquals(expectedNodes, actualNodes);
  }

  @ParameterizedTest
  @CsvSource ({
      "1, 14",
//      "2, 191",
//      "3, 2812",
//      "4, 43238",
//      "5, 674624"
  })
  void testPositionThree(int depth, long expectedNodes) {
    Board board = new ChessBoard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
    long actualNodes = runPerft(board, depth, PieceColor.WHITE);
    Assertions.assertEquals(expectedNodes, actualNodes);
  }

  @ParameterizedTest
  @CsvSource ({
      "1, 6",
//      "2, 264",
//      "3, 9467",
//      "4, 422333",
//      "5, 15833292"
  })
  void testPositionFour(int depth, long expectedNodes) {
    Board board = new ChessBoard("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
    long actualNodes = runPerft(board, depth, PieceColor.WHITE);
    Assertions.assertEquals(expectedNodes, actualNodes);
  }

  @ParameterizedTest
  @CsvSource ({
      "1, 44",
//      "2, 1486",
//      "3, 62379",
//      "4, 2103487",
//      "5, 89941194"
  })
  void testPositionFive(int depth, long expectedNodes) {
    Board board = new ChessBoard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
    long actualNodes = runPerft(board, depth, PieceColor.WHITE);
    Assertions.assertEquals(expectedNodes, actualNodes);
  }

  @ParameterizedTest
  @CsvSource ({
      "1, 46",
//      "2, 2079",
//      "3, 89890",
//      "4, 3894594",
//      "5, 164075551"
  })
  void testPositionSix(int depth, long expectedNodes) {
    Board board = new ChessBoard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
    long actualNodes = runPerft(board, depth, PieceColor.WHITE);
    Assertions.assertEquals(expectedNodes, actualNodes);
  }
}