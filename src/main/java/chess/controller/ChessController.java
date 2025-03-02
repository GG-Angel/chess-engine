//package chess.controller;
//
//import static java.util.Objects.requireNonNull;
//
//import chess.model.board.ChessBoard;
//import chess.model.move.ChessMove;
//import chess.view.View;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class ChessController implements Controller {
//  private ChessControllerState state;
//  private ChessBoard model;
//  private View view;
//  private Readable readable;
//  private Scanner scanner;
//
//  public ChessController(ChessBoard model, View view, Readable readable) throws NullPointerException {
//    this.model = requireNonNull(model, "Must pass non-null Model to Controller.");
//    this.view = requireNonNull(view, "Must pass non-null View to Controller.");
//    this.readable = requireNonNull(readable, "Must pass non-null Readable to Controller.");
//    this.scanner = new Scanner(readable);
//  }
//
//  public ChessController(ChessBoard model, View view) throws NullPointerException {
//    this(model, view, new InputStreamReader(System.in));
//  }
//
//  public void playGame() throws IllegalArgumentException {
//    try {
//      view.renderBoard();
//      while (true) {
//        getUserInput();
//        view.renderBoard();
//        view.renderMessage("");
//      }
//    } catch (IOException e) {
//      throw new IllegalStateException("Failed to communicate with View class or read input.");
//    }
//  }
//
//  public void getUserInput() throws IOException {
//    try {
//      String input = scanner.nextLine();
//
//      int[] coordinates = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).toArray();
//      int fromRow = coordinates[0] - 1;
//      int fromCol = coordinates[1] - 1;
//      int toRow = coordinates[2] - 1;
//      int toCol = coordinates[3] - 1;
//
//      model.movePiece(new ChessMove(fromRow, fromCol, toRow, toCol));
//    } catch (Exception e) {
//      view.renderMessage(e.getMessage());
//    }
//  }
//}
