package chess;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.model.piece.PieceColor;
import chess.view.ChessTextView;
import chess.view.TextView;
import java.io.IOException;

public class Chess {

  public static void main(String[] args) {
    Board board = new ChessBoard("r3k2r/p1ppqpb1/1n2pnp1/1b1PN3/1p2P3/2N2Q1p/PPPB1PPP/R3K2R w KQkq - 0 2");
    board.legalMovesPerft(3, PieceColor.WHITE);
  }
}
