package chess;

import chess.model.board.Board;
import chess.model.board.ChessBoard;
import chess.model.move.Move;
import chess.view.ChessTextView;
import chess.view.View;

import java.io.IOException;
import java.util.List;

public class Chess {

  public static void main(String[] args) throws IOException {
//    Board board = new ChessBoard();
    Board board = new ChessBoard("8/P7/8/8/8/8/8/8 w - - 6 18");
    View view = new ChessTextView(board);

    view.renderBoard();

    long timeStart = System.nanoTime();
    List<Move> legalMoves = board.generateLegalMoves();
    long timeEnd = System.nanoTime();

    long elapsedTime = timeEnd - timeStart;

    view.renderMessage(board.generateLegalMoves().toString());
    view.renderMessage("TIME (ms): " + (elapsedTime / 1000000.0));
  }
}
