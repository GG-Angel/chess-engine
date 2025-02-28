package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.Move;
import java.util.ArrayList;

public abstract class Piece implements IPiece {
  protected PieceColor color;
  protected PieceType type;
  protected ArrayList<Move> validMoves;

  protected Piece(PieceColor color, PieceType type) {
    this.color = color;
    this.type = type;
    this.validMoves = new ArrayList<>();
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
  public ArrayList<Move> getValidMoves() {
    return this.validMoves;
  }

  public abstract void computeValidMoves(int row, int col, ChessBoard board);
}
