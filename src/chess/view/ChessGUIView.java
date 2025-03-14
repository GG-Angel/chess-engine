package chess.view;

import static chess.model.ChessBoard.BOARD_SIZE;
import static java.util.Objects.requireNonNull;
import static utilities.Utils.to1D;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.model.Move;
import chess.model.piece.ChessPiece;
import chess.model.piece.Piece;
import chess.model.piece.PieceConstants;
import chess.model.piece.PieceLookup;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChessGUIView extends Application {
  private static Board board;

  private final Color colorBeige = Color.web("#f0dab5");
  private final Color colorBrown = Color.web("#b58763");
  private final Color colorMark = Color.web("#ff8282", 0.6);
  private final Color colorMove = Color.web("#53a0ed", 0.6);

  private final int SQUARE_SIZE = 72;
  private final GridPane grid = new GridPane();
  private final Rectangle[] squareMarkers = new Rectangle[64];

  public ChessGUIView() {
    requireNonNull(board, "Must pass non-null Board to View.");
  }

  @Override
  public void start(Stage stage) throws Exception {
    if (board == null) {
      throw new IllegalStateException("Board must be set before starting the view.");
    }

    // create 8x8 grid
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        int position = to1D(x, y);
        String positionStr = position + "";

        StackPane stackPane = new StackPane();
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);

        // alternate square colors
        boolean isLightSquare = (y + x) % 2 == 0;
        if (isLightSquare) {
          square.setFill(colorBeige); // beige
        } else {
          square.setFill(colorBrown); // brown
        }

        // add square color
        stackPane.getChildren().add(square);

        // add mark on right click
        Rectangle squareMark = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        squareMark.setId(positionStr);
        squareMark.setFill(Color.TRANSPARENT);
        stackPane.setOnMouseClicked(e -> highlightOnClick(e, squareMark));
        stackPane.getChildren().add(squareMark);
        squareMarkers[position] = squareMark;

        // add coordinate to square
        Label coordinate = new Label(positionStr);
        StackPane.setAlignment(coordinate, Pos.TOP_LEFT);
        coordinate.setFont(new Font(12));
        coordinate.setTextFill(isLightSquare ? colorBrown : colorBeige);
        coordinate.setPadding(new Insets(2, 0, 0, 2));
        stackPane.getChildren().add(coordinate);

        Piece pieceAtSquare = board.getPieceAtPosition(position);
        if (!ChessPiece.isEmpty(pieceAtSquare)) {
          // load piece image
          String pieceImagePath = PieceLookup.getImage(pieceAtSquare.getColor(), pieceAtSquare.getType());
          Image pieceImage = new Image(pieceImagePath);
          ImageView pieceImageView = new ImageView(pieceImage);

          // scale to fit square
          pieceImageView.setFitWidth(SQUARE_SIZE);
          pieceImageView.setFitHeight(SQUARE_SIZE);

          // add piece on top of square
          stackPane.getChildren().add(pieceImageView);
        }

        // add square to board and store reference
        grid.add(stackPane, x, y);
      }
    }

    Scene scene = new Scene(grid, SQUARE_SIZE * 8, SQUARE_SIZE * 8);
    stage.setTitle("Chess Engine");
    stage.setScene(scene);
    stage.show();
  }

  private void highlightOnClick(MouseEvent e, Rectangle square) {
    if (e.getButton() == MouseButton.PRIMARY) {
      for (Rectangle marker : squareMarkers) {
        marker.setFill(Color.TRANSPARENT);
      }

      Piece piece = board.getPieceAtPosition(Integer.parseInt(square.getId()));
      if (piece != null) {
//        List<Move> moves = piece.calculatePseudoLegalMoves(board);
//        for (Move move : moves) {
//          squareMarkers[move.getTo()].setFill(colorMove);
//        }
        piece.calculatePseudoLegalMoves(board);
        List<Integer> attacks = piece.getAttackingPositions();
        for (int pos : attacks) {
          squareMarkers[pos].setFill(colorMove);
        }
      }
    } else if (e.getButton() == MouseButton.SECONDARY) {
      if (square.getFill() == Color.TRANSPARENT) {
        square.setFill(colorMark); // mark
      } else {
        square.setFill(Color.TRANSPARENT); // unmark
      }
    }
  }

  public static void main(String[] args) {
    board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
    System.out.println(Arrays.toString(PieceConstants.NUM_SQUARES_FROM_EDGE[28]));
    launch(args);
  }
}
