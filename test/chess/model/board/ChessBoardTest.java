package chess.model.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ChessBoardTest {

  @DisplayName("Performance Test for Generating Legal Moves")
  @ParameterizedTest
  @CsvSource ({
    "0, 1",
    "1, 20",
    "2, 400",
    "3, 8902",
    "4, 197281",
    "5, 4865609",
  })
  void testGenerateLegalMovesPerformance(int depth, long expectedNodes) {
    Board board = new ChessBoard();
    long actualNodes = board.performanceTest(depth);
    Assertions.assertEquals(expectedNodes, actualNodes);
  }
}