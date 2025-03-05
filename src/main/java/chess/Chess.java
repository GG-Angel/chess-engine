package chess;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.piece.Piece;
import chess.view.ChessTextView;
import chess.view.View;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    ChessBoard board = new ChessBoard("rn3rk1/pbppq1pQ/1p2pb2/4N3/3PN3/3B4/PPP2PPP/R3K2R b KQ - 0 11");
    View view = new ChessTextView(board);

    view.renderBoard();
    view.renderMessage(board.generateLegalMoves().toString());
  }
}
