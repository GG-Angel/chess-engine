package chess.model.piece;

import static java.util.Objects.requireNonNull;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import java.util.ArrayList;

public abstract class ChessPiece implements Piece {
  public static ChessPiece createPiece(PieceColor color, PieceType type) throws NullPointerException {
    return switch (type) {
      case PAWN -> new ChessPawn(color);
      case BISHOP -> new ChessBishop(color);
      case KNIGHT -> new ChessKnight(color);
      case ROOK -> new ChessRook(color);
      case QUEEN -> new ChessQueen(color);
      case KING -> new ChessKing(color);
    };
  }

  protected PieceColor color;
  protected PieceType type;
  protected ArrayList<ChessMove> validMoves;
  protected boolean hasMoved;

  protected ChessPiece(PieceColor color, PieceType type) throws NullPointerException {
    this.color = requireNonNull(color, "Must pass non-null Color to Piece.");
    this.type = requireNonNull(type, "Must pass non-null Type to Piece.");
    this.validMoves = new ArrayList<>();
    this.hasMoved = false;
  }

  @Override
  public PieceColor getColor() {
    return this.color;
  }

  @Override
  public PieceType getType() {
    return this.type;
  }

  @Override
  public ArrayList<ChessMove> getValidMoves() {
    return this.validMoves;
  }

  public abstract void computeValidMoves(int fromRow, int fromCol, ChessBoard board);
}
