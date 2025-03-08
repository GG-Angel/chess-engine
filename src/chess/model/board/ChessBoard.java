package chess.model.board;

import chess.model.move.ChessMove;
import chess.model.move.Move;
import chess.model.piece.ChessPiece;
import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import chess.model.piece.PieceType;

import java.util.*;

import static chess.model.piece.ChessPiece.createPiece;
import static chess.model.piece.PieceColor.BLACK;
import static chess.model.piece.PieceColor.WHITE;
import static chess.model.piece.PieceType.*;

public class ChessBoard implements Board {
  private final int boardSize;
  private final Piece[][] board;
  private final Map<PieceColor, Set<Piece>> pieces;
  private final Map<PieceColor, List<Move>> moves;

  private PieceColor turn;
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
    this.pieces = new HashMap<>();
    this.moves = new HashMap<>();
    this.moveStack = new Stack<>();
    this.checkStack = new Stack<>();
    this.halfMoveClock = new Stack<>();

    this.pieces.put(WHITE, new HashSet<>());
    this.pieces.put(BLACK, new HashSet<>());
    this.moves.put(WHITE, new ArrayList<>());
    this.moves.put(BLACK, new ArrayList<>());
    this.checkStack.push(false);

    initializeBoard(fen);
  }

  private void initializeBoard(String fen) {
    try {
      String[] fenFields = fen.split(" ");

      this.turn = fenFields[1].equals("w") ? WHITE : BLACK;
      this.halfMoveClock.push(Integer.parseInt(fenFields[4]));
      this.fullMoveClock = Integer.parseInt(fenFields[5]);

      initializeBoardPieces(fenFields[0]);
      initializeCastling(fenFields[2]);
      initializeEnPassant(fenFields[3]);

      generateKingCheck(turn);
      generateMoves(getOpposingColor(this.turn));
      generateLegalMoves(this.turn);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid FEN string: " + e.getMessage());
    }
  }

  private void initializeBoardPieces(String fenBoard) {
    Map<Character, PieceType> symbolToPieceType = new HashMap<>();
    symbolToPieceType.put('p', PAWN);
    symbolToPieceType.put('b', BISHOP);
    symbolToPieceType.put('n', KNIGHT);
    symbolToPieceType.put('r', ROOK);
    symbolToPieceType.put('q', QUEEN);
    symbolToPieceType.put('k', KING);

    char[] fenBoardChars = fenBoard.toCharArray();
    int row = 0; int col = 0;
    for (char symbol : fenBoardChars) {
      if (symbol == '/') {
        row++;
        col = 0;
      } else {
        if (Character.isDigit(symbol)) {
          col += Character.getNumericValue(symbol);
        } else {
          PieceType type = symbolToPieceType.get(Character.toLowerCase(symbol));
          PieceColor color = Character.isUpperCase(symbol) ? WHITE : BLACK;
          Piece piece = createPiece(color, type, row, col);
          pieces.get(color).add(piece);
          this.board[row][col] = piece;
          col++;
        }
      }
    }
  }

  private void initializeCastling(String fenCastling) throws IllegalArgumentException {
    if (!fenCastling.equals("-")) {
      char[] cannotCastle = "KQkq".replaceAll("[" + fenCastling + "]", "").toCharArray();
      for (char symbol : cannotCastle) {
        int rank = Character.isUpperCase(symbol) ? 7 : 0;
        int rookCol = Character.toLowerCase(symbol) == 'k' ? 7 : 0;
        Piece piece = this.board[rank][rookCol];
        if (piece != null && piece.getType() == ROOK) {
          piece.setHasMovedBefore(true);
        }
      }
    }
  }

  private void initializeEnPassant(String fenEnPassant) throws IllegalArgumentException {
    if (!fenEnPassant.equals("-")) {
      int targetRow = 9 - Integer.parseInt(String.valueOf(fenEnPassant.charAt(1)));
      int pawnCol = ((int) fenEnPassant.charAt(0)) - 97;

      if (isOutOfBounds(targetRow, pawnCol)) {
        throw new IllegalArgumentException("En passant target square is out of bounds.");
      }

      Piece targetPawn = this.board[targetRow][pawnCol];
      if (targetPawn != null && targetPawn.getType() == PAWN && (targetRow == 3 || targetRow == 4)) {
        int homeRow = targetPawn.getColor() == WHITE ? 6 : 1;
        Move pawnMove = new ChessMove(homeRow, pawnCol, targetPawn, targetRow, pawnCol, null);
        moveStack.add(pawnMove);
      } else {
        throw new IllegalArgumentException("En passant target square does not point to a valid pawn.");
      }
    }
  }

  private List<Move> generateMoves(PieceColor side) {
    List<Move> generatedMoves = new ArrayList<>();
    for (Piece piece : pieces.get(side)) {
      if (piece.isAlive()) {
        piece.setValidMoves(piece.computeMoves(this));
        generatedMoves.addAll(piece.getValidMoves());
      }
    }
    moves.put(side, generatedMoves);
    return generatedMoves;
  }

  @Override
  public List<Move> generateLegalMoves(PieceColor side) {
    List<Move> pseudoLegalMoves = generateMoves(side);
    Map<Piece, List<Move>> legalMoves = new HashMap<>();

    // set up legal moves map
    for (Piece piece : pieces.get(side)) {
      if (piece.isAlive()) {
        legalMoves.put(piece, new ArrayList<>());
      }
    }

    // if this move is valid, store it for its associated piece
    for (Move moveToVerify : pseudoLegalMoves) {
      makeMove(moveToVerify);
      if (!checkKingCheck(side)) {
        Piece piece = moveToVerify.fromPiece();
        legalMoves.get(piece).add(moveToVerify);
      }
      undoMove();
    }

    // set the legal moves for each piece
    List<Move> allLegalMoves = new ArrayList<>();
    for (Map.Entry<Piece, List<Move>> entry : legalMoves.entrySet()) {
      Piece piece = entry.getKey();
      List<Move> legalPieceMoves = entry.getValue();
      piece.setValidMoves(legalPieceMoves);
      allLegalMoves.addAll(legalPieceMoves);
    }

    moves.put(side, allLegalMoves);
    return allLegalMoves;
  }

  private boolean generateKingCheck(PieceColor side) {
    boolean isKingInCheck = checkKingCheck(side);
    this.checkStack.push(isKingInCheck);
    return isKingInCheck;
  }

  private boolean checkKingCheck(PieceColor side) {
    PieceColor opponentColor = getOpposingColor(side);
    List<Move> opponentMoves = moves.get(opponentColor);
    return opponentMoves.stream().anyMatch(Move::threatensKing);
  }

  @Override
  public long legalMovesPerft(int depth) {
    if (depth == 0) {
      return 1;
    }

    long nodes = 0;
    List<Move> legalMoves = generateLegalMoves(turn);

    for (Move move : legalMoves) {
      makeMove(move);
      nodes += legalMovesPerft(depth - 1);
      undoMove();
    }

    return nodes;
  }

  @Override
  public boolean isValidMove(Move move) {
    return move != null && move.fromPiece().getValidMoves().contains(move);
  }

  @Override
  public void validateMove(Move move) {
    if (!isValidMove(move)) {
      throw new IllegalArgumentException(String.format("Suggested move on board is not valid: %s", move.toString()));
    }
  }

  @Override
  public void makeMove(Move move) throws IllegalArgumentException, NullPointerException {
    // store move for backtracking
    moveStack.push(move);
    executeMakeMove(move);

    if (move.fromPiece().getType() == PAWN || move.toPiece() != null) {
      this.halfMoveClock.push(0);
    } else {
      this.halfMoveClock.push(this.halfMoveClock.peek() + 1);
    }

    if (turn == BLACK) {
      this.fullMoveClock++;
    }

    switchTurn();
    generateKingCheck(turn);
  }

  private void executeMakeMove(Move move) {
    Piece fromPiece = move.fromPiece();
    Piece toPiece = move.toPiece();

    // move the piece to new position on board
    this.board[move.fromRow()][move.fromCol()] = null;
    this.board[move.toRow()][move.toCol()] = fromPiece;
    if (toPiece != null) {
      toPiece.setIsAlive(false);
    }

    // update piece position
    fromPiece.setPosition(move.toRow(), move.toCol());
    fromPiece.setHasMovedBefore(true);

    // check for move chains
    if (move.getSubMove() != null) {
      executeMakeMove(move.getSubMove());
    }
  }

  @Override
  public void undoMove() throws IllegalStateException {
    if (moveStack.isEmpty()) {
      throw new IllegalStateException("Cannot undo move when stack is empty.");
    }

    Move lastMove = moveStack.pop();
    executeUndoMove(lastMove);

    this.halfMoveClock.pop();
    if (turn == WHITE) {
      this.fullMoveClock--;
    }

    switchTurn();
    checkStack.pop();
  }

  private void executeUndoMove(Move move) {
    if (move.getSubMove() != null) {
      executeUndoMove(move.getSubMove());
    }

    Piece fromPiece = move.fromPiece();
    Piece toPiece = move.toPiece();

    // put pieces back in place
    this.board[move.fromRow()][move.fromCol()] = fromPiece;
    this.board[move.toRow()][move.toCol()] = toPiece;
    if (toPiece != null) {
      toPiece.setIsAlive(true);
    }

    // update piece to previous position
    fromPiece.setPosition(move.fromRow(), move.fromCol());

    // restore previous has moved state
    if (move.wasFirstMove()) {
      move.fromPiece().setHasMovedBefore(false);
    }
  }

  private void switchTurn() {
    this.turn = this.turn == WHITE ? BLACK : WHITE;
  }

  @Override
  public Map<PieceColor, Set<Piece>> getPieces() {
    return this.pieces;
  }

  @Override
  public Map<PieceColor, List<Move>> getMoves() {
    return this.moves;
  }

  @Override
  public PieceColor getOpposingColor(PieceColor color) {
    return color == WHITE ? BLACK : WHITE;
  }

  @Override
  public boolean isKingInCheck() {
    return this.checkStack.peek();
  }

  @Override
  public Stack<Move> getMoveStack() {
    return this.moveStack;
  }

  @Override
  public int getBoardSize() {
    return this.boardSize;
  }

  @Override
  public int getHalfMoves() {
    return this.halfMoveClock.peek();
  }

  @Override
  public int getFullMoves() {
    return this.fullMoveClock;
  }

  @Override
  public Piece getPieceAt(int row, int col) throws IndexOutOfBoundsException {
    validateBounds(row, col);
    return this.board[row][col];
  }

  @Override
  public boolean isOutOfBounds(int row, int col) {
    return row < 0 || row >= getBoardSize() || col < 0 || col >= getBoardSize();
  }

  @Override
  public void validateBounds(int row, int col) throws IndexOutOfBoundsException {
    if (isOutOfBounds(row, col)) {
      throw new IndexOutOfBoundsException(String.format(
          "Cannot access row or column out of bounds: (%d, %d)", row, col
      ));
    }
  }
}
