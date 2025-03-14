package chess.view;

import java.io.IOException;

public interface TextView {

  void renderBoard() throws IOException;

  void renderMessage(String message) throws IOException;

  void renderNewLine() throws IOException;
}
