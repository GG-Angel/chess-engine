package chess.view;

import static chess.model.ChessBoard.BOARD_SIZE;
import static chess.utilities.Utils.getPositionFile;
import static chess.utilities.Utils.getPositionRank;
import static java.util.Objects.requireNonNull;
import static chess.utilities.Utils.to1D;

import chess.model.Board;
import chess.model.ChessBoard;
import chess.model.Move;
import chess.model.piece.ChessPiece;
import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import chess.model.piece.PieceLookup;
import java.util.List;
import java.util.Objects;
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
import javafx.scene.text.FontWeight;
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
  public void start(Stage stage) {
    if (board == null) {
      throw new IllegalStateException("Board must be set before starting the view.");
    }

    refreshBoard();

    Scene scene = new Scene(grid, SQUARE_SIZE * 8, SQUARE_SIZE * 8);
    stage.setTitle("Chess Engine");
    stage.setScene(scene);
    stage.show();
  }

  private void refreshBoard() {
    grid.getChildren().clear();
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        int position = to1D(x, y);
        String positionStr = position + "";
        StackPane stackPane = new StackPane();

        // create alternating squares
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        boolean isLightSquare = (y + x) % 2 == 0;
        square.setFill(isLightSquare ? colorBeige : colorBrown);
        stackPane.getChildren().add(square);

        // add coordinate to square
        Label coordinate = new Label(positionStr);
        StackPane.setAlignment(coordinate, Pos.TOP_LEFT);
        coordinate.setFont(new Font(12));
        coordinate.setTextFill(isLightSquare ? colorBrown : colorBeige);
        coordinate.setPadding(new Insets(2, 0, 0, 2));
        stackPane.getChildren().add(coordinate);

        // add ranks and files to square if necessary
        if (x == 0) {
          Label rank = new Label(getPositionRank(position) + "");
          StackPane.setAlignment(rank, Pos.BOTTOM_LEFT);
          rank.setFont(Font.font("System", FontWeight.BOLD, 12));
          rank.setTextFill(Color.BLACK);
          rank.setPadding(new Insets(0, 0, 2, 2));
          stackPane.getChildren().add(rank);
        }

        if (y == 7) {
          Label rank = new Label(getPositionFile(position) + "");
          StackPane.setAlignment(rank, Pos.BOTTOM_RIGHT);
          rank.setFont(Font.font("System", FontWeight.BOLD, 12));
          rank.setTextFill(Color.BLACK);
          rank.setPadding(new Insets(0, 2, 2, 0));
          stackPane.getChildren().add(rank);
        }

        // create square marker
        Rectangle squareMark = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        squareMark.setId(positionStr);
        squareMark.setFill(Color.TRANSPARENT);
        stackPane.setOnMouseClicked(event -> highlightOnClick(event, squareMark));
        stackPane.getChildren().add(squareMark);
        squareMarkers[position] = squareMark;

        // add piece to square if it exists
        Piece pieceAtSquare = board.getPieceAtPosition(position);
        if (!ChessPiece.isEmpty(pieceAtSquare)) {
          String pieceImagePath = PieceLookup.getImage(pieceAtSquare.getColor(), pieceAtSquare.getType());
          Image pieceImage = new Image(pieceImagePath);
          ImageView pieceImageView = new ImageView(pieceImage);
          pieceImageView.setFitWidth(SQUARE_SIZE);
          pieceImageView.setFitHeight(SQUARE_SIZE);

          // TODO: remove later
          if (pieceAtSquare.hasMoved()) {
            squareMark.setFill(colorMark);
          }

          stackPane.getChildren().add(pieceImageView);
        }

        stackPane.setId(positionStr);
        grid.add(stackPane, x, y);
      }
    }
  }

  private void highlightOnClick(MouseEvent e, Rectangle square) {
    if (e.getButton() == MouseButton.PRIMARY) {
      for (Rectangle marker : squareMarkers) {
        marker.setFill(Color.TRANSPARENT);
      }

      Piece piece = board.getPieceAtPosition(Integer.parseInt(square.getId()));
      if (!ChessPiece.isEmpty(piece)) {
        for (Move move : piece.getPseudoLegalMoves()) {
          squareMarkers[move.getTo()].setFill(colorMove);
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
    board = new ChessBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
//    board = new ChessBoard();

    List<Move> whiteMoves = board.generatePseudoLegalMoves(PieceColor.WHITE);
    List<Move> blackMoves = board.generatePseudoLegalMoves(PieceColor.BLACK);

    launch(args);
  }
}
