package chess.model.piece;

import static chess.model.piece.PieceConstants.NUM_SQUARES_FROM_EDGE;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PieceConstantsTest {

  @ParameterizedTest
  @CsvSource ({
      "25, '3, 1, 6, 4, 1, 3, 1, 4'",
      "13, '1, 5, 2, 6, 1, 1, 5, 2'",
      "1,  '0, 1, 6, 7, 0, 0, 1, 6'"
  })
  void testNumSquaresFromEdge(int position, String expected) {
    String[] expectedDistances = expected.split(", ");
    for (int dirOffset = 0; dirOffset < 8; dirOffset++) {
      Assertions.assertEquals(
          Integer.parseInt(expectedDistances[dirOffset]),
          NUM_SQUARES_FROM_EDGE[position][dirOffset]
      );
    }
  }
}