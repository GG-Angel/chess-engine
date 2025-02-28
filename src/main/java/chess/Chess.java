package chess;

import chess.controller.ChessController;
import chess.controller.Controller;
import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.view.ChessTextView;
import chess.view.View;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    ChessBoard board = new ChessBoard();
    View view = new ChessTextView(board);
    ChessController controller = new ChessController(board, view);

    controller.playGame();
  }
}
