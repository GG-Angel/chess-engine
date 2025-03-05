package chess.model.board;

import static chess.model.piece.ChessPiece.createPiece;
import static chess.model.piece.ChessPiece.getOpposingColor;
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

import java.util.*;

public class ChessBoard {
  private final Piece[][] board;
  private final int boardSize;

  private PieceColor turnColor;
  private final Stack<Move> moveStack;
  private final Stack<Boolean> checkStack;;
  private final Stack<Integer> halfMoveClock;
  private int fullMoveClock;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.boardSize = 8;
    this.board = new ChessPiece[boardSize][boardSize];
    this.moveStack = new Stack<>();
    this.checkStack = new Stack<>();
    this.halfMoveClock = new Stack<>();
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

      String[] fenFields = fen.split(" ");
      this.turnColor = fenFields[1].equals("w") ? WHITE : BLACK;
      this.halfMoveClock.push(Integer.parseInt(fenFields[4]));
      this.fullMoveClock = Integer.parseInt(fenFields[5]);
      // missing en passant target square, can make one move in stack for it and its done!

      char[] fenBoard = fenFields[0].toCharArray();
      String fenCastling = fenFields[2];

      // place pieces on board
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
            this.board[row][col] = createPiece(color, type);
            col++;
          }
        }
      }

      // assign castling permissions
      if (!fenCastling.equals("-")) {
        char[] cannotCastle = "KQkq".replaceAll("[" + fenCastling + "]", "").toCharArray();
        for (char symbol : cannotCastle) {
          int rank = Character.isUpperCase(symbol) ? 7 : 0;
          int rookCol = Character.toLowerCase(symbol) == 'k' ? 7 : 0;
          Piece piece = this.board[rank][rookCol];
          if (piece != null && piece.getType() == ROOK) {
            piece.setHasMoved(true);
          }
        }
      }

      this.checkStack.push(isKingInCheck(turnColor));
      generateLegalMoves();
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid FEN string: " + e.getMessage());
    }
  }

  private boolean isKingInCheck(PieceColor side) {
    PieceColor opponentColor = getOpposingColor(side);
    for (int row = 0; row < getBoardSize(); row++) {
      for (int col = 0; col < getBoardSize(); col++) {
        Piece piece = this.board[row][col];
        if (piece != null && piece.getColor() == opponentColor && piece.getType() != KING) {
          List<Move> potentialNextMoves = piece.computeMoves(row, col, this);
          if (potentialNextMoves.stream().anyMatch(Move::threatensKing)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private List<Move> generateMoves() {
    List<Move> generatedMoves = new ArrayList<>();
    for (int row = 0; row < getBoardSize(); row++) {
      for (int col = 0; col < getBoardSize(); col++) {
        Piece piece = this.board[row][col];
        if (piece != null && piece.getColor() == turnColor) {
          piece.setValidMoves(piece.computeMoves(row, col, this));
          generatedMoves.addAll(piece.getValidMoves());
        }
      }
    }
    return generatedMoves;
  }

  public List<Move> generateLegalMoves() {
    List<Move> pseudoLegalMoves = generateMoves();
    List<Move> legalMoves = new ArrayList<>();
    PieceColor currentTurnColor = turnColor;

    for (Move moveToVerify : pseudoLegalMoves) {
      makeMove(moveToVerify);
      if (!isKingInCheck(currentTurnColor)) {
        legalMoves.add(moveToVerify);
      }
      undoMove();
    }

    return legalMoves;
  }

  public void makeMove(Move move) throws IllegalArgumentException, NullPointerException {
    requireNonNull(move, "Suggested move on board cannot be null.");
    List<Move> validMoves = move.fromPiece().getValidMoves();
    if (!validMoves.contains(move)) {
      throw new IllegalArgumentException(String.format("Suggested move on board is not valid: %s", move));
    }

    // store move for backtracking
    moveStack.push(move);
    executeMakeMove(move);

    if (move.fromPiece().getType() == PAWN || move.toPiece() != null) {
      this.halfMoveClock.push(0);
    } else {
      this.halfMoveClock.push(this.halfMoveClock.peek() + 1);
    }

    if (turnColor == BLACK) {
      this.fullMoveClock++;
    }

    switchTurn();
    this.checkStack.push(isKingInCheck(turnColor));
  }

  private void executeMakeMove(Move move) {
    // move the piece to new position on board
    this.board[move.fromRow()][move.fromCol()] = null;
    this.board[move.toRow()][move.toCol()] = move.fromPiece();
    move.fromPiece().setHasMoved(true);

    // check for move chains
    if (move.getSubMove() != null) {
      executeMakeMove(move.getSubMove());
    }
  }

  public void undoMove() throws IllegalStateException {
    if (moveStack.isEmpty()) {
      throw new IllegalStateException("Cannot undo move when stack is empty.");
    }

    Move lastMove = moveStack.pop();
    executeUndoMove(lastMove);

    this.halfMoveClock.pop();
    if (turnColor == WHITE) {
      this.fullMoveClock--;
    }

    switchTurn();
    checkStack.pop();
  }

  private void executeUndoMove(Move move) {
    if (move.getSubMove() != null) {
      executeUndoMove(move.getSubMove());
    }

    // put pieces back in place
    this.board[move.fromRow()][move.fromCol()] = move.fromPiece();
    this.board[move.toRow()][move.toCol()] = move.toPiece();

    // restore previous has moved state
    if (move.wasFirstMove()) {
      move.fromPiece().setHasMoved(false);
    }
  }

  private void switchTurn() {
    this.turnColor = this.turnColor == WHITE ? BLACK : WHITE;
  }

  public Stack<Move> getMoveStack() {
    return this.moveStack;
  }

  public boolean isCurrentKingInCheck() {
    return this.checkStack.peek();
  }

  public int getBoardSize() {
    return this.boardSize;
  }

  public int getHalfMoves() {
    return this.halfMoveClock.peek();
  }

  public int getFullMoves() {
    return this.fullMoveClock;
  }

  public Piece getPieceAt(int row, int col) throws IndexOutOfBoundsException {
    validateBounds(row, col);
    return this.board[row][col];
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
