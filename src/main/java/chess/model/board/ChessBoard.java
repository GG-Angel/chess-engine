package chess.model.board;

import static chess.model.piece.ChessPiece.createPiece;
import static chess.model.piece.PieceColor.BLACK;
import static chess.model.piece.PieceColor.WHITE;
import static chess.model.piece.PieceType.BISHOP;
import static chess.model.piece.PieceType.KING;
import static chess.model.piece.PieceType.KNIGHT;
import static chess.model.piece.PieceType.PAWN;
import static chess.model.piece.PieceType.QUEEN;
import static chess.model.piece.PieceType.ROOK;
import static java.util.Objects.requireNonNull;

import chess.model.move.Move;
import chess.model.piece.Piece;
import chess.model.piece.ChessPiece;
import chess.model.piece.PieceType;
import java.util.ArrayList;
import java.util.Stack;

public class ChessBoard {
  private final Piece[][] board;
  private final ArrayList<Piece> pieces;
  private final Stack<Move> moveStack;
  private final int boardSize;

  public ChessBoard() {
    this.boardSize = 8;
    this.board =  new ChessPiece[boardSize][boardSize];
    this.pieces = new ArrayList<>();
    this.moveStack = new Stack<>();
    initializeBoard();
  }

  private void initializeBoard() {
    PieceType[] order = { ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK };
    for (int col = 0; col < getBoardSize(); col++) {
      // create back ranks
      PieceType type = order[col];
      this.board[0][col] = createPiece(BLACK, type);
      this.board[7][col] = createPiece(WHITE, type);

      // create pawn rows
      this.board[1][col] = createPiece(BLACK, PAWN);
      this.board[6][col] = createPiece(WHITE, PAWN);
    }

    // calculate initial valid moves, store references to pieces
    int[] rows = new int[] { 0, 1, 6, 7 };
    for (int row : rows) {
      for (int col = 0; col < getBoardSize(); col++) {
        Piece piece = this.board[row][col];
        piece.computeValidMoves(row, col, this);
        this.pieces.add(piece);
      }
    }
  }

  public void movePiece(Piece piece, Move move) throws IllegalArgumentException, NullPointerException {
    requireNonNull(move, "Suggested move on board cannot be null.");
    if (!piece.getValidMoves().contains(move)) {
      throw new IllegalArgumentException("Suggested move on board is not valid.");
    }
  }

  public Piece getPieceAt(int row, int col) throws IndexOutOfBoundsException {
    validateBounds(row, col);
    return this.board[row][col];
  }

  public int getBoardSize() {
    return this.boardSize;
  }

  public boolean isInBounds(int row, int col) {
    return row >= 0 && row < getBoardSize() && col >= 0 && col < getBoardSize();
  }

  public void validateBounds(int row, int col) throws IndexOutOfBoundsException {
    if (!isInBounds(row, col)) {
      throw new IndexOutOfBoundsException(String.format(
          "Cannot access row or column out of bounds: (%d, %d)", row, col
      ));
    }
  }
}
