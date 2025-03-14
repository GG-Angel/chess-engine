package chess;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.model.Move;
import chess.model.piece.PieceColor;
import chess.view.ChessTextView;
import chess.view.TextView;
import java.io.IOException;
import chess.utilities.Utils;
import java.util.List;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    TextView view = new ChessTextView(board);

    List<Move> whiteMoves = board.generatePseudoLegalMoves(PieceColor.WHITE);
    List<Move> blackMoves = board.generatePseudoLegalMoves(PieceColor.BLACK);

    for (Move move : whiteMoves) {
      System.out.println(move);
      if (move.toString().equals("e1g1")) {
        board.makeMove(move);
      }
    }

    view.renderBoard();
  }
}
