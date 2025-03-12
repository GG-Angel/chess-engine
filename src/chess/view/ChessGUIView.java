package chess.view;

import static chess.model.ChessBoard.BOARD_SIZE;
import static java.util.Objects.requireNonNull;
import static utilities.Utils.to1D;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.model.piece.ChessPiece;
import chess.model.piece.Piece;
import chess.model.piece.PieceLookup;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
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
        StackPane stackPane = new StackPane();

        Rectangle square = new Rectangle(squareSize, squareSize);

        // alternate square colors
        boolean isLightSquare = (y + x) % 2 == 0;
        Color beige = Color.web("#f0dab5");
        Color brown = Color.web("#b58763");
        if (isLightSquare) {
          square.setFill(beige); // beige
        } else {
          square.setFill(brown); // brown
        }

        // add square color
        stackPane.getChildren().add(square);

        // add coordinate to square
        Label coordinateLabel = new Label(to1D(x, y) + "");
        coordinateLabel.setFont(new Font(12));
        coordinateLabel.setTextFill(isLightSquare ? brown : beige);
        coordinateLabel.setPadding(new Insets(2, 0, 0, 2));
        stackPane.setAlignment(coordinateLabel, Pos.TOP_LEFT);
        stackPane.getChildren().add(coordinateLabel);

        Piece pieceAtSquare = board.getPieceAtPosition(to1D(x, y));
        if (!ChessPiece.isEmpty(pieceAtSquare)) {
          // load piece image
          String pieceImagePath = PieceLookup.getImage(pieceAtSquare.getColor(), pieceAtSquare.getType());
          Image pieceImage = new Image(pieceImagePath);
          ImageView pieceImageView = new ImageView(pieceImage);

          // scale to fit square
          pieceImageView.setFitWidth(squareSize);
          pieceImageView.setFitHeight(squareSize);

          // add piece on top of square
          stackPane.getChildren().add(pieceImageView);
        }

        // add square to board
        chessBoard.add(stackPane, x, y);
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
