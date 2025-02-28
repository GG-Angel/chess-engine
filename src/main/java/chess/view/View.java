package chess.view;

import java.io.IOException;

public interface View {

  public void renderBoard() throws IOException;

  public void renderMessage(String message) throws IOException;
}
