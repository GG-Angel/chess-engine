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
//    Board board = new ChessBoard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
//    View view = new ChessTextView(board);
//
//    List<Move> moves = board.generateLegalMoves(PieceColor.WHITE);
//    board.makeMove(moves.get(2));
//    moves = board.generateLegalMoves(PieceColor.BLACK);
//
//    view.renderBoard();
//    for (Move move : moves) {
//      System.out.print(move.toString() + ": ");
//      board.makeMove(move);
//      List<Move> nodes = board.generateLegalMoves(PieceColor.WHITE);
//      System.out.print(nodes.size() + "\n");
//      board.undoMove();
//    }
//
//    // 20
//
//    board.makeMove(moves.get(20));
//
//    view.renderBoard();
//    view.renderMessage("" + board.checkKingCheck(PieceColor.BLACK));
//    view.renderMessage(board.getPieceAt(0, 2).computeMoves(board).toString());
//    view.renderMessage(board.getPieceAt(0, 2).computeMoves(board).get(4).threatensKing() + "");
//    view.renderMessage(board.checkKingCheck(PieceColor.BLACK) + "");

    Board board = new ChessBoard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
    View view = new ChessTextView(board);
  }
}
