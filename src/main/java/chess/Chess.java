package chess;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.view.ChessTextView;
import chess.view.View;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    ChessBoard board = new ChessBoard();
    View view = new ChessTextView(board);

    board.movePiece(new ChessMove(6, 4, 4, 4, board));
    board.movePiece(new ChessMove(1, 4, 3, 4, board));
    board.movePiece(new ChessMove(7, 6, 5, 5, board));
    board.movePiece(new ChessMove(0, 5, 3, 2, board));
    board.movePiece(new ChessMove(5, 5, 3, 4, board));
    board.movePiece(new ChessMove(1, 3, 3, 3, board));
    board.movePiece(new ChessMove(4, 4, 3, 3, board));
    view.renderBoard();
  }
}
