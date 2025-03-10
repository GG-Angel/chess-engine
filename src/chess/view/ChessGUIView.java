package chess.view;

import static java.util.Objects.requireNonNull;

import chess.model.Board;

public class ChessGUIView {
  private final Board board;

  public ChessGUIView(Board board) throws NullPointerException {
    this.board = requireNonNull(board, "Must pass non-null Model to View.");
  }


}
