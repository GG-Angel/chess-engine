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
import chess.model.piece.PieceColor;
import chess.model.piece.PieceType;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

public class ChessBoard {
  private final Piece[][] board;
  private final ArrayList<Piece> whitePieces;
  private final ArrayList<Piece> blackPieces;
  private final Stack<Move> moveStack;
  private final int boardSize;

  public ChessBoard() {
    this.boardSize = 8;
    this.board =  new ChessPiece[boardSize][boardSize];
    this.whitePieces = new ArrayList<>();
    this.blackPieces = new ArrayList<>();
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
        piece.computeMoves(row, col, this);
        if (row <= 1) {
          this.blackPieces.add(piece);
        } else {
          this.whitePieces.add(piece);
        }
      }
    }
  }

  public void movePiece(Move move) throws IllegalArgumentException, NullPointerException {
    requireNonNull(move, "Suggested move on board cannot be null.");
    if (!move.fromPiece().getValidMoves().contains(move)) {
      throw new IllegalArgumentException("Suggested move on board is not valid.");
    }
    executeMovePiece(move);
  }

  private void executeMovePiece(Move move) {
    // move the piece to new position on board
    this.board[move.fromRow()][move.fromCol()] = null;
    this.board[move.toRow()][move.toCol()] = move.fromPiece();
    move.fromPiece().setHasMoved(true);

    // kill an enemy piece if taken
    if (move.toPiece() != null) {
      PieceColor color = move.toPiece().getColor();
      getFriendlyPieces(color).remove(move.toPiece());
    }

    // store move for backtracking
    moveStack.push(move);

    // check for move chains
    if (move.getSubMove() != null) {
      executeMovePiece(move.getSubMove());
    } else {
      refreshValidMoves();
    }
  }

  private void refreshValidMoves() {
    // TODO: Find more efficient means of recomputing valid moves.
    //       Maybe there's a way that only recomputes the affected pieces?

    for (int row = 0; row < this.boardSize; row++) {
      for (int col = 0; col < this.boardSize; col++) {
        Piece piece = this.board[row][col];
        if (piece != null) {
          piece.computeMoves(row, col, this);
        }
      }
    }
  }

  // TODO: Handle checks/wins in transition by checking if the king does/doesn't have valid moves.

  public Piece getPieceAt(int row, int col) throws IndexOutOfBoundsException {
    validateBounds(row, col);
    return this.board[row][col];
  }

  public Stack<Move> getMoveStack() {
    return this.moveStack;
  }

  public ArrayList<Piece> getFriendlyPieces(PieceColor color) {
    return color == PieceColor.WHITE ? whitePieces : blackPieces;
  }

  public ArrayList<Piece> getOpposingPieces(PieceColor color) {
    return color == PieceColor.WHITE ? blackPieces : whitePieces ;
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
