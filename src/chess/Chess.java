package chess;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.view.ChessTextView;
import chess.view.TextView;
import java.io.IOException;
import utilities.Utils;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard("8/3k4/5r2/6Q1/8/8/3K4/8 w - - 0 1");
    TextView view = new ChessTextView(board);
    view.renderBoard();
    view.renderMessage(board.getPieceAtPosition(Utils.to1D(6, 3)).calculatePseudoLegalMoves(board).toString());
  }
}
