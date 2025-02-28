package chess;

import chess.model.board.ChessBoard;
import chess.view.ChessTextView;
import chess.view.IView;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    ChessBoard board = new ChessBoard();
    IView view = new ChessTextView(board);

    view.renderBoard();
  }
}
