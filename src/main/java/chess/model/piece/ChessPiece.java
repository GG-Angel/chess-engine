package chess.model.piece;

import chess.model.board.ChessBoard;
import chess.model.move.ChessMove;
import java.util.ArrayList;

public abstract class ChessPiece implements Piece {
  protected PieceColor color;
  protected PieceType type;
  protected ArrayList<ChessMove> validMoves;

  protected ChessPiece(PieceColor color, PieceType type) {
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
  public ArrayList<ChessMove> getValidMoves() {
    return this.validMoves;
  }

  public abstract void computeValidMoves(int fromRow, int fromCol, ChessBoard board);
}
