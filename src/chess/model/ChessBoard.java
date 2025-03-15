package chess.model;

import static chess.model.piece.PieceColor.*;
import static chess.utilities.Utils.convertRankFileToPosition;
import static chess.utilities.Utils.to1D;

import chess.model.piece.ChessPiece;
import chess.model.piece.Piece;
import chess.model.piece.PieceColor;
import chess.model.piece.PieceFactory;
import chess.model.piece.PieceLookup;
import chess.model.piece.PieceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class ChessBoard implements Board {
  public static int BOARD_SIZE = 8;

  private final Piece[] board;
  private final Map<PieceColor, List<Piece>> pieces;
  private final Map<PieceColor, Piece> kings;
  private final Stack<Piece> capturedPieces;

  private int enPassantTarget;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.board = new Piece[64];
    this.pieces = new HashMap<>();
    this.kings = new HashMap<>();
    this.capturedPieces = new Stack<>();
    this.enPassantTarget = -1;

    this.pieces.put(WHITE, new ArrayList<>());
    this.pieces.put(BLACK, new ArrayList<>());

    initializeBoardFromFen(fen);

    // prepare move generation
    generatePseudoLegalMoves(WHITE);
    generatePseudoLegalMoves(BLACK);
    for (Piece king : getKings()) {
      king.calculatePseudoLegalMoves(this);
    }
  }

  @Override
  public List<Move> generatePseudoLegalMoves(PieceColor color) {
    List<Move> pseudoLegalMoves = new ArrayList<>();
    for (Piece piece : getPieces(color)) {
      pseudoLegalMoves.addAll(piece.calculatePseudoLegalMoves(this));
    }
    return pseudoLegalMoves;
  }

  @Override
  public List<Move> generateLegalMoves(PieceColor color) {
    List<Move> pseudoLegalMoves = generatePseudoLegalMoves(color);
    List<Move> legalMoves = new ArrayList<>();

    // TODO: Improve
    for (Move moveToVerify : pseudoLegalMoves) {
      makeMove(moveToVerify);
      generatePseudoLegalMoves(getEnemyColor(color));
      if (!isKingInCheck(color)) {
        legalMoves.add(moveToVerify);
      }
      unMakeMove(moveToVerify);
    }

    return legalMoves;
  }

  @Override
  public boolean isKingInCheck(PieceColor color) throws IllegalStateException {
    int kingPosition = getKing(color).getPosition();
    Set<Integer> enemyPositionsControlled = getPositionsControlled(getEnemyColor(color));
    return enemyPositionsControlled.contains(kingPosition);
  }

  private void movePiece(int from, int to, Piece piece) {
    board[to] = piece;
    board[from] = null;
    piece.setPosition(to);
  }

  @Override
  public void makeMove(Move move) {
    int from = move.getFrom();
    int to = move.getTo();
    Piece piece = move.getPiece();
    MoveType moveType = move.getMoveType();

    switch (moveType) {
      case NORMAL -> {
        killPiece(getPieceAtPosition(to));

        movePiece(from, to, piece);
        piece.setHasMoved(true);
      }

      case CASTLING -> {
        Move rookMove = move.getCastlingMove();
        Piece rook = rookMove.getPiece();

        movePiece(from, to, piece);
        movePiece(rookMove.getFrom(), rookMove.getTo(), rook);

        piece.setHasMoved(true);
        rook.setHasMoved(true);
      }

      case PROMOTION -> {
        Piece promotionPiece = move.getPromotionPiece();

        killPiece(getPieceAtPosition(to));

        movePiece(from, to, promotionPiece);
        promotionPiece.setHasMoved(true);
      }

      case EN_PASSANT -> {
        int enPassantPawnPosition = move.getEnPassantPawnPosition();
        Piece enPassantPawn = getPieceAtPosition(enPassantPawnPosition);

        killPiece(enPassantPawn);
        board[enPassantPawnPosition] = null;

        movePiece(from, to, piece);
      }
    }
  }

  @Override
  public void unMakeMove(Move move) {
    int from = move.getFrom();
    int to = move.getTo();
    Piece piece = move.getPiece();
    MoveType moveType = move.getMoveType();
    boolean hasPieceMovedBefore = move.hasPieceMovedBefore();

    // move the piece back to its original position
    switch (moveType) {
      case NORMAL -> {
        movePiece(to, from, piece);
        piece.setHasMoved(hasPieceMovedBefore);
      }

      case CASTLING -> {
        Move rookMove = move.getCastlingMove();
        Piece rook = rookMove.getPiece();

        movePiece(to, from, piece);
        movePiece(rookMove.getTo(), rookMove.getFrom(), rook);

        piece.setHasMoved(false);
        rook.setHasMoved(false);
      }

      case PROMOTION -> {
        Piece promotionPiece = move.getPromotionPiece();
        removePieceFromLookup(promotionPiece);

        movePiece(to, from, piece);
      }

      case EN_PASSANT -> {
        movePiece(to, from, piece);
      }
    }

    // restore captured piece
    if (moveType != MoveType.PROMOTION) {
      Piece capturedPiece = revivePiece();
      if (!ChessPiece.isEmpty(capturedPiece)) {
        int capturedPiecePosition = capturedPiece.getPosition();
        board[capturedPiecePosition] = capturedPiece;
      }
    }
  }

  @Override
  public long legalMovesPerft(int depth, PieceColor startingColor) {
    if (depth == 0) {
      return 1;
    }

    long nodes = 0;
    List<Move> legalMoves = generateLegalMoves(startingColor);

    for (Move move : legalMoves) {
      makeMove(move);
      nodes += legalMovesPerft(depth - 1, getEnemyColor(startingColor));
      unMakeMove(move);
    }

    return nodes;
  }

  @Override
  public Set<Integer> getPositionsControlled(PieceColor color) {
    Set<Integer> positionsControlled = new HashSet<>();
    for (Piece piece : getPieces(color)) {
      positionsControlled.addAll(piece.getPositionsControlled());
    }
    return positionsControlled;
  }

  @Override
  public Piece getPieceAtPosition(int position) {
    return this.board[position];
  }

  @Override
  public int getEnPassantTarget() {
    return enPassantTarget;
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

        // store references for lookup
        if (type == PieceType.KING) { kings.put(color, piece); }
        addPieceToLookup(piece);
      }
    }

    this.enPassantTarget = !fenParams[3].equals("-") ? convertRankFileToPosition(fenParams[3]) : -1;
  }

  private List<Piece> getPieces(PieceColor color) {
    return pieces.get(color);
  }

  private Piece getKing(PieceColor color) {
    return kings.get(color);
  }

  private List<Piece> getKings() {
    return kings.values().stream().toList();
  }

  private void addPieceToLookup(Piece piece) {
    pieces.get(piece.getColor()).add(piece);
  }

  private void removePieceFromLookup(Piece piece) {
    pieces.get(piece.getColor()).remove(piece);
  }

  private void killPiece(Piece piece) {
    if (!ChessPiece.isEmpty(piece)) {
      removePieceFromLookup(piece);
    }
    capturedPieces.push(piece);
  }

  private Piece revivePiece() {
    Piece piece = capturedPieces.pop();

    if (!ChessPiece.isEmpty(piece)) {
      addPieceToLookup(piece);
    }

    return piece;
  }
}