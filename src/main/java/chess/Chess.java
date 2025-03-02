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
    board.movePiece(new ChessMove(4, 4, 3, 4, board));
    board.movePiece(new ChessMove(1, 3, 3, 3, board));

    view.renderBoard();
    view.renderMessage(board.getPieceAt(3, 4).getValidMoves().toString());
    view.renderMessage("");

    board.movePiece(board.getPieceAt(3, 4).getValidMoves().getLast());

    view.renderBoard();
  }
}
