package chess;

import chess.model.board.Board;
import chess.model.board.ChessBoard;
import chess.model.move.Move;
import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import chess.model.piece.PieceType;
import chess.view.ChessTextView;
import chess.view.View;

import java.io.IOException;
import java.util.List;

public class Chess {

  public static void main(String[] args) throws IOException {
    Board board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    View view = new ChessTextView(board);

    List<Move> moves = board.generateLegalMoves(PieceColor.WHITE);

    view.renderBoard();
    for (Move move : moves) {
      System.out.print(move.toString() + ": ");
      board.makeMove(move);
      List<Move> nodes = board.generateLegalMoves(PieceColor.BLACK);
      System.out.print(nodes.size() + "\n");
      board.undoMove();
    }

    // e5d7
    for (Move move : moves) {
      if (move.toString().equals("e5d7")) {
        board.makeMove(move);
        break;
      }
    }

    moves = board.generateLegalMoves(PieceColor.BLACK);

    view.renderBoard();
    for (Move move : moves) {
      System.out.print(move.toString() + ": ");
      board.makeMove(move);
      List<Move> nodes = board.generateLegalMoves(PieceColor.WHITE);
      System.out.print(nodes.size() + "\n");
      board.undoMove();
    }
  }
}
