package chess.model;

import static chess.model.piece.PieceColor.*;
import static chess.utilities.Utils.convertRankFileToPosition;
import static chess.utilities.Utils.to1D;

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

    Set<Integer> enemyPositionsControlled = getPositionsControlled(getEnemyColor(color));

    return enemyPositionsControlled.contains(kingPosition);
  }

  @Override
  public void makeMove(Move move) {
    switch (move.getMoveType()) {
      case NORMAL -> {
        movePiece(move.getFrom(), move.getTo(), move.getPiece());
      }
      case CASTLING -> {
        Move rookMove = move.getCastlingMove();
        movePiece(move.getFrom(), move.getTo(), move.getPiece());
        movePiece(rookMove.getFrom(), rookMove.getTo(), rookMove.getPiece());
      }
      case PROMOTION -> {
        board[move.getTo()] = move.getPromotionPiece();
        board[move.getFrom()] = null;
      }
      case EN_PASSANT -> {
        movePiece(move.getFrom(), move.getTo(), move.getPiece());
        board[move.getEnPassantPawnPosition()] = null;
      }
    }
  }

  private void movePiece(int from, int to, Piece piece) {
    board[to] = piece;
    board[from] = null;
    piece.setPosition(to);
    piece.setHasMoved(true);
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
        addPiece(piece);
      }
    }

    // TODO: assign has moved based on if a piece's position is different from its initial position

    this.enPassantTarget = !fenParams[3].equals("-") ? convertRankFileToPosition(fenParams[3]) : -1;

    // prepare move generation
    generatePseudoLegalMoves(WHITE);
    generatePseudoLegalMoves(BLACK);
    for (Piece king : getKings()) {
      king.calculatePseudoLegalMoves(this);
    }
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

  private List<Piece> getKings() {
    return kings.values().stream().toList();
  }
}