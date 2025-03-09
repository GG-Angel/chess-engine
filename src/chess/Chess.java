package chess;

import chess.model.board.Board;
import chess.model.board.ChessBoard;
import chess.model.move.Move;
import chess.model.piece.PieceColor;
import chess.view.ChessTextView;
import chess.view.View;

import java.io.IOException;
import java.util.List;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
    View view = new ChessTextView(board);
    view.renderBoard();

    List<Move> moves = board.generateLegalMoves(PieceColor.WHITE);
    for (Move move : moves) {
      System.out.println(move + ": 1");
    }
  }
}
