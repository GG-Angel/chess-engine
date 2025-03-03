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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    initializeBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.boardSize = 8;
    this.board =  new ChessPiece[boardSize][boardSize];
    this.whitePieces = new ArrayList<>();
    this.blackPieces = new ArrayList<>();
    this.moveStack = new Stack<>();
    initializeBoard(fen);
  }

  private void initializeBoard(String fen) {
    try {
      Map<Character, PieceType> symbolToPieceType = new HashMap<>();
      symbolToPieceType.put('p', PAWN);
      symbolToPieceType.put('b', BISHOP);
      symbolToPieceType.put('n', KNIGHT);
      symbolToPieceType.put('r', ROOK);
      symbolToPieceType.put('q', QUEEN);
      symbolToPieceType.put('k', KING);

      char[] fenBoard = fen.split(" ")[0].toCharArray();
      int row = 0; int col = 0;

      for (char symbol : fenBoard) {
        if (symbol == '/') {
          row++;
          col = 0;
        } else {
          if (Character.isDigit(symbol)) {
            col += Character.getNumericValue(symbol);
          } else {
            PieceType type = symbolToPieceType.get(Character.toLowerCase(symbol));
            PieceColor color = Character.isUpperCase(symbol) ? WHITE : BLACK;
            Piece piece = createPiece(color, type);
            getFriendlyPieces(color).add(piece);
            this.board[row][col] = piece;
            col++;
          }
        }
      }

      generateMoves();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid FEN string.");
    }
  }

  private void generateMoves() {
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
      generateMoves();
    }
  }

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

  public boolean isOutOfBounds(int row, int col) {
    return row < 0 || row >= getBoardSize() || col < 0 || col >= getBoardSize();
  }

  public void validateBounds(int row, int col) throws IndexOutOfBoundsException {
    if (isOutOfBounds(row, col)) {
      throw new IndexOutOfBoundsException(String.format(
          "Cannot access row or column out of bounds: (%d, %d)", row, col
      ));
    }
  }
}
