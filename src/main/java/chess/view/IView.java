package chess.view;

import java.io.IOException;

public interface IView {

  public void renderBoard() throws IOException;

  public void renderMessage(String message) throws IOException;
}
