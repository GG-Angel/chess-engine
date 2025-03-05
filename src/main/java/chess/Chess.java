package chess;

import chess.model.board.Board;
import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.piece.Piece;
import chess.view.ChessTextView;
import chess.view.View;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard("8/P7/8/8/8/8/8/8 w - - 0 1");
    View view = new ChessTextView(board);

    view.renderBoard();
    view.renderMessage(board.generateLegalMoves().toString());
  }
}
