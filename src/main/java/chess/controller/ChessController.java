package chess.controller;

import static java.util.Objects.requireNonNull;

import chess.model.board.ChessBoard;
import chess.view.View;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ChessController implements Controller {
  private ChessBoard model;
  private View view;
  private Readable readable;
  private Scanner scanner;

  public ChessController(ChessBoard model, View view, Readable readable) throws NullPointerException {
    this.model = requireNonNull(model, "Must pass non-null Model to Controller.");
    this.view = requireNonNull(view, "Must pass non-null View to Controller.");
    this.readable = requireNonNull(readable, "Must pass non-null Readable to Controller.");
    this.scanner = new Scanner(readable);
  }

  public ChessController(ChessBoard model, View view) throws NullPointerException {
    this(model, view, new InputStreamReader(System.in));
  }
}
