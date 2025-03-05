package chess;

import chess.model.board.Board;
import chess.model.board.ChessBoard;
import chess.view.ChessTextView;
import chess.view.View;

import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard("rnb1kbnr/pppp1ppp/8/4p3/4P2q/2N5/PPPP1PPP/R1BQKBNR w KQkq - 2 3");
    View view = new ChessTextView(board);

    view.renderBoard();
    view.renderMessage(board.getPieceAt(6, 5).getValidMoves().toString());
  }
}
