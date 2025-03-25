package chess;

import static chess.MoveGenerator.generatePseudoLegalMoves;
import static chess.Piece.Color.WHITE;
import static org.junit.jupiter.api.Assertions.*;

import chess.Piece.Color;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// https://www.chessprogramming.org/Perft_Results
class MoveGeneratorTest {


  @ParameterizedTest
  @CsvSource ({
      "1, 20"
  })
  void testInitialPosition(int depth, int expected) {
    Board board = new Board();
    List<Move> moves = generatePseudoLegalMoves(board, WHITE);
    Assertions.assertEquals(expected, moves.size());
  }
}