package chess;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import chess.model.piece.Piece;
import chess.view.ChessTextView;
import chess.view.View;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) throws IOException {
    ChessBoard board = new ChessBoard();
    View view = new ChessTextView(board);

    view.renderBoard();
    view.renderMessage("");

//    // en passant
//    board.movePiece(new ChessMove(6, 4, 4, 4, board, null));
//    board.movePiece(new ChessMove(4, 4, 3, 4, board, null));
//    board.movePiece(new ChessMove(1, 3, 3, 3, board, null));
//    view.renderBoard();
//    view.renderMessage(board.getPieceAt(3, 4).getValidMoves().toString());
//    view.renderMessage("");
//    board.movePiece(board.getPieceAt(3, 4).getValidMoves().getLast());
//    view.renderBoard();

    // king side castling
//    Piece king = board.getPieceAt(7, 4);
//
//    board.movePiece(new ChessMove(6, 4, 4, 4, board, null));
//    board.movePiece(new ChessMove(7, 5, 4, 2, board, null));
//    board.movePiece(new ChessMove(7, 6, 5, 5, board, null));
//
//    view.renderBoard();
//    view.renderMessage(king.getValidMoves().toString());
//    view.renderMessage("");
//
//    board.movePiece(king.getValidMoves().getLast());
//
//    view.renderBoard();
//    view.renderMessage(king.getValidMoves().toString());

//    // queen side castling
//    Piece king = board.getPieceAt(7, 4);
//
//    board.movePiece(new ChessMove(7, 1, 5, 2, board, null));
//    board.movePiece(new ChessMove(6, 3, 5, 3, board, null));
//    board.movePiece(new ChessMove(7, 2, 4, 5, board, null));
//    board.movePiece(new ChessMove(7, 3, 6, 3, board, null));
//
//    view.renderBoard();
//    view.renderMessage(king.getValidMoves().toString());
//    view.renderMessage("");
//
//    board.movePiece(king.getValidMoves().getLast());
//
//    view.renderBoard();
//    view.renderMessage(king.getValidMoves().toString());

    // queen side castling
    Piece king = board.getPieceAt(7, 4);

    board.movePiece(new ChessMove(7, 1, 5, 2, board, null));
    board.movePiece(new ChessMove(6, 3, 5, 3, board, null));
    board.movePiece(new ChessMove(7, 2, 4, 5, board, null));
    board.movePiece(new ChessMove(7, 3, 6, 3, board, null));

    view.renderBoard();
    view.renderMessage(king.getValidMoves().toString());
    view.renderMessage("");

    board.movePiece(king.getValidMoves().getLast());

    view.renderBoard();
    view.renderMessage(king.getValidMoves().toString());
  }
}
