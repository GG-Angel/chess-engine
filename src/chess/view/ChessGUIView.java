package chess.view;

import static chess.model.ChessBoard.BOARD_SIZE;
import static java.util.Objects.requireNonNull;
import static utilities.Utils.to1D;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.model.piece.ChessPiece;
import chess.model.piece.Piece;
import chess.model.piece.PieceLookup;
import com.sun.javafx.iio.ImageStorageException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ChessGUIView extends Application {
  private static Board board;

  public ChessGUIView() {
    requireNonNull(board, "Must pass non-null Board to View.");
  }

  @Override
  public void start(Stage stage) throws Exception {
    if (board == null) {
      throw new IllegalStateException("Board must be set before starting the view.");
    }

    GridPane chessBoard = new GridPane();
    int squareSize = 64;

    // create 8x8 grid
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        Rectangle square = new Rectangle(squareSize, squareSize);

        // alternate square colors
        if ((y + x) % 2 == 0) {
          square.setFill(Color.web("#f0dab5")); // beige
        } else {
          square.setFill(Color.web("#b58763")); // brown
        }

        // add square to board
        chessBoard.add(square, x, y);

        Piece pieceAtSquare = board.getPieceAtPosition(to1D(x, y));
        if (!ChessPiece.isEmpty(pieceAtSquare)) {
          // load piece image
          String pieceImagePath = PieceLookup.getImage(pieceAtSquare.getColor(), pieceAtSquare.getType());
          Image pieceImage = new Image(pieceImagePath);
          ImageView pieceImageView = new ImageView(pieceImage);

          // scale to fit square
          pieceImageView.setFitWidth(squareSize);
          pieceImageView.setFitHeight(squareSize);

          // add piece image onto square
          chessBoard.add(pieceImageView, x, y);
        }
      }
    }

    Scene scene = new Scene(chessBoard, 512, 512);
    stage.setTitle("Chess Engine");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    launch(args);
  }
}
