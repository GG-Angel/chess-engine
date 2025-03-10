package chess.model;

import static chess.model.piece.PieceColor.*;
import static utilities.Utils.to1D;

import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import chess.model.piece.PieceFactory;
import chess.model.piece.PieceLookup;
import chess.model.piece.PieceType;
import java.util.Arrays;

public class ChessBoard implements Board {
  public static int BOARD_SIZE = 8;

  private final Piece[] board;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.board = new Piece[64];
    initializeBoardFromFen(fen);
    System.out.println(Arrays.toString(board));
  }

  private void initializeBoardFromFen(String fen) {
    String[] fenParams = fen.split(" ");

    int x = 0, y = 0;
    char[] fenBoard = fenParams[0].toCharArray();
    for (char symbol : fenBoard) {
      if (symbol == '/') {
        x = 0;
        y++;
      } else if (Character.isDigit(symbol)) {
        x += Character.getNumericValue(symbol);
      } else {
        int position = to1D(x, y);
        PieceColor color = Character.isUpperCase(symbol) ? WHITE : BLACK;
        PieceType type = PieceLookup.getType(symbol);
        Piece piece = PieceFactory.createPiece(color, type, position);
        board[position] = piece;
        x++;
      }
    }
  }

  @Override
  public Piece getPieceAtPosition(int position) {
    return this.board[position];
  }
}