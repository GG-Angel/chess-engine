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
    Board board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    View view = new ChessTextView(board);

    List<Move> moves = board.generateLegalMoves(PieceColor.WHITE);

    for (Move move : moves) {
      if (move.toString().equals("d5d6")) {
        board.makeMove(move);
      }
    }

    moves = board.generateLegalMoves(PieceColor.BLACK);

    for (Move move : moves) {
      if (move.toString().equals("f6d5")) {
        board.makeMove(move);
      }
    }

    moves = board.generateLegalMoves(PieceColor.WHITE);

    for (Move move : moves) {
      if (move.toString().equals("d6e7")) {
        board.makeMove(move);
      }
    }

    moves = board.generateLegalMoves(PieceColor.BLACK);

    view.renderBoard();

    for (Move move : moves) {
      System.out.print(move.toString() + ": ");
      board.makeMove(move);
      List<Move> nodes = board.generateLegalMoves(PieceColor.WHITE);
      System.out.print(nodes.size() + "\n");
//      System.out.println(board.legalMovesPerft(1));
      board.undoMove();
    }
  }
}
