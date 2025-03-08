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
//    Board board = new ChessBoard("rnbqkbnr/pppppppp/8/8/5q2/8/PPPPP1PP/RNBQK2R w KQkq - 0 1");
//    View view = new ChessTextView(board);
//
//    view.renderBoard();
//    view.renderMessage(board.getPieceAt(7, 4).computeMoves(board).toString());

    Board board = new ChessBoard();
  }
}
