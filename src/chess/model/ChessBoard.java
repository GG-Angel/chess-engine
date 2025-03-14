package chess.model;

import static chess.model.piece.PieceColor.*;
import static utilities.Utils.convertRankFileToPosition;
import static utilities.Utils.to1D;

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
import java.util.Optional;
import java.util.Set;

public class ChessBoard implements Board {
  public static int BOARD_SIZE = 8;

  private final Piece[] board;
  private final Map<PieceColor, List<Piece>> pieces;
  private final Map<PieceColor, Piece> kings;

  private int enPassantTarget;

  public ChessBoard() {
    this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public ChessBoard(String fen) {
    this.board = new Piece[64];
    this.pieces = new HashMap<>();
    this.kings = new HashMap<>();
    this.enPassantTarget = -1;

    this.pieces.put(WHITE, new ArrayList<>());
    this.pieces.put(BLACK, new ArrayList<>());

    initializeBoardFromFen(fen);
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
  public boolean isKingInCheck(PieceColor color) throws IllegalStateException {
    int kingPosition = getKing(color).getPosition();

    Set<Integer> enemyControlledPositions = new HashSet<>();
    for (Piece enemyPiece : getPieces(getEnemyColor(color))) {
      enemyControlledPositions.addAll(enemyPiece.getAttackingPositions());
    }

    return enemyControlledPositions.contains(kingPosition);
  }

  @Override
  public void makeMove(Move move) {
    switch (move.getMoveType()) {
      case NORMAL -> {
        board[move.getTo()] = move.getPiece();
        board[move.getFrom()] = null;
        move.getPiece().setPosition(move.getTo());
      }
      case CASTLING -> {
        // TODO: Implement make castling move
      }
      case PROMOTION -> {
        board[move.getTo()] = move.getPromotionPiece();
        board[move.getFrom()] = null;
      }
      case EN_PASSANT -> {
        board[move.getTo()] = move.getPiece();
        board[move.getFrom()] = null;
        board[move.getEnPassantPawnPosition()] = null;
        move.getPiece().setPosition(move.getTo());
      }
    }
  }

  @Override
  public Piece getPieceAtPosition(int position) {
    return this.board[position];
  }

  @Override
  public int getEnPassantTarget() {
    return enPassantTarget;
  }

  @Override
  public void setEnPassantTarget(int position) {
    enPassantTarget = position;
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
        addPiece(piece);
      }
    }

    this.enPassantTarget = !fenParams[3].equals("-") ? convertRankFileToPosition(fenParams[3]) : -1;
  }

  private List<Piece> getPieces(PieceColor color) {
    return pieces.get(color);
  }

  private void addPiece(Piece piece) {
    pieces.get(piece.getColor()).add(piece);
  }

  private void removePiece(Piece piece) {
    pieces.get(piece.getColor()).remove(piece);
  }

  private Piece getKing(PieceColor color) {
    return kings.get(color);
  }
}