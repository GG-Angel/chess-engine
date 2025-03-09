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
    Board board = new ChessBoard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
    View view = new ChessTextView(board);

    List<Move> moves = board.generateLegalMoves(PieceColor.WHITE);

//    view.renderBoard();
//    for (Move move : moves) {
//      System.out.print(move.toString() + ": ");
//      board.makeMove(move);
//      List<Move> nodes = board.generateLegalMoves(PieceColor.BLACK);
//      System.out.print(nodes.size() + "\n");
//      board.undoMove();
//    }

    // d7c8q
    for (Move move : moves) {
      if (move.toString().equals("d7c8q")) {
        System.out.println(move.fromPiece() + " -> " + move.toPiece());
        System.out.println(move.getSubMove().fromPiece() + " -> " + move.getSubMove().toPiece());
        board.makeMove(move);
        break;
      }
    }

    System.out.println("PROMOTED!");

    moves = board.generateLegalMoves(PieceColor.BLACK);

//    view.renderBoard();
//    for (Move move : moves) {
//      System.out.print(move.toString() + ": ");
//      board.makeMove(move);
//      List<Move> nodes = board.generateLegalMoves(PieceColor.WHITE);
//      System.out.print(nodes.size() + "\n");
//      board.undoMove();
//    }

    view.renderMessage(board.getPieceAt(0, 3).getValidMoves().toString());

    // d8d5
    for (Move move : moves) {
      if (move.toString().equals("d8d5")) {
        board.makeMove(move);
        break;
      }
    }

//    view.renderMessage("KING CHECK? " + board.checkKingCheck(PieceColor.BLACK));
//    view.renderMessage("QUEEN ALIVE? " + board.getPieceAt(0, 2).isAlive());

    moves = board.generateLegalMoves(PieceColor.WHITE);

    view.renderBoard();
    for (Move move : moves) {
      System.out.print(move.toString() + ": ");
      board.makeMove(move);
      List<Move> nodes = board.generateLegalMoves(PieceColor.BLACK);
      System.out.print(nodes.size() + "\n");
      board.undoMove();
    }

  }
}
