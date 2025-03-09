package chess;

import chess.model.board.Board;
import chess.model.board.ChessBoard;
import chess.view.ChessTextView;
import chess.view.View;

import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard();
    View view = new ChessTextView(board);
    view.renderBoard();
  }
}
