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
    Board board = new ChessBoard("2p6/1P6/8/8/8/8/8/8 w KQkq - 0 1");
    View view = new ChessTextView(board);

    view.renderBoard();

    long timeStart = System.nanoTime();
    List<Move> legalMoves = board.generateLegalMoves();
    long timeEnd = System.nanoTime();

    long elapsedTime = timeEnd - timeStart;

    view.renderMessage(legalMoves.toString());
    view.renderMessage("TIME (ms): " + (elapsedTime / 1000000.0));

    board.makeMove(board.getPieceAt(1, 1).getValidMoves().getFirst());
    board.undoMove();

    view.renderBoard();
  }
}
