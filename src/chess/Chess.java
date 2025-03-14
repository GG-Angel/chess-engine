package chess;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.model.piece.PieceColor;
import chess.view.ChessTextView;
import chess.view.TextView;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard();
    TextView view = new ChessTextView(board);

    System.out.println("Legal White Moves: " + board.generateLegalMoves(PieceColor.WHITE).size());

    view.renderBoard();
  }
}
