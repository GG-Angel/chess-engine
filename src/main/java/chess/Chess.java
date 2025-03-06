package chess;

import chess.model.board.Board;
import chess.model.board.ChessBoard;
import chess.view.ChessTextView;
import chess.view.View;

import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard("rn3r2/pbppq1p1/1p2pN2/8/3P2NP/6P1/PPPKBP1R/R5k1 b - - 6 18");
    View view = new ChessTextView(board);

    view.renderBoard();
    view.renderMessage(board.generateLegalMoves().toString());
  }
}
