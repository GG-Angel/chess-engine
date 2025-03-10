package chess.view;

import chess.model.Board;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public abstract class ChessView implements TextView {
  private final Appendable appendable;
  protected final Board board;

  protected ChessView(Board board) throws NullPointerException {
    this(board, System.out);
  }

  protected ChessView(Board board, Appendable appendable) throws NullPointerException {
    this.board = requireNonNull(board, "Must pass non-null Model to View.");
    this.appendable = requireNonNull(appendable, "Must pass non-null Appendable to View.");
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

  @Override
  public void renderNewLine() throws IOException {
    this.appendable.append("\n");
  }
}
