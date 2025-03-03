package chess.view;

import static java.util.Objects.requireNonNull;

import chess.model.board.ChessBoard;
import java.io.IOException;

public abstract class ChessView implements View {
  private final Appendable appendable;
  protected final ChessBoard board;

  protected ChessView(ChessBoard board) throws NullPointerException {
    this(board, System.out);
  }

  protected ChessView(ChessBoard board, Appendable appendable) throws NullPointerException {
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
