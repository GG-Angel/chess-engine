package chess;

import static chess.model.ChessBoard.BOARD_SIZE;
import static chess.model.piece.ChessPiece.directionOffsets;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.view.ChessTextView;
import chess.view.View;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard();
    View view = new ChessTextView(board);
    view.renderBoard();

    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < directionOffsets.length; j++) {

      }
    }
  }
}
