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
    Board board = new ChessBoard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
    View view = new ChessTextView(board);

    view.renderBoard();
    List<Move> moves = board.generateLegalMoves(PieceColor.WHITE);
    for (Move move : moves) {
      view.renderMessage(move.toString());
    }
  }
}
