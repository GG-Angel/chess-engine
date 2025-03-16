package chess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChessBoardTest {

  @Test
  void testInitializeBoard() {
    ChessBoard board = new ChessBoard();
    String expected = """
        1  r n b q k b n r\s
        2  p p p p p p p p\s
        3  . . . . . . . .\s
        4  . . . . . . . .\s
        5  . . . . . . . .\s
        6  . . . . . . . .\s
        7  P P P P P P P P\s
        8  R N B Q K B N R\s
        
           A B C D E F G H""";
    Assertions.assertEquals(expected, board.toString());
  }
  
}