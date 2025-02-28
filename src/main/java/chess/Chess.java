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

    board.movePiece(new ChessMove(6, 6, 4, 6, board));
    board.movePiece(new ChessMove(1, 4, 3, 4, board));
    board.movePiece(new ChessMove(6, 5, 5, 5, board));
    board.movePiece(new ChessMove(0, 3, 4, 7, board));
    view.renderBoard();
    System.out.println(board.getPieceAt(7, 4).getValidMoves());
  }
}
