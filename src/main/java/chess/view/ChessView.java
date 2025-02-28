package chess.view;

import chess.model.board.ChessBoard;
import java.io.IOException;

public abstract class ChessView implements View {
  private final Appendable appendable;
  protected final ChessBoard board;

  protected ChessView(ChessBoard board) {
    this(board, System.out);
  }

  protected ChessView(ChessBoard board, Appendable appendable) throws IllegalArgumentException {
    if (board == null) {
      throw new IllegalArgumentException("Must pass non-null Board state to View.");
    }

    if (appendable == null) {
      throw new IllegalArgumentException("Must pass non-null Appendable to View.");
    }

    this.board = board;
    this.appendable = appendable;
  }

  @Override
  public void renderBoard() throws IOException {
    this.appendable
        .append(this.toString())
        .append("\n");
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.appendable
        .append(message)
        .append("\n");
  }
}
