package chess.model.piece;

import chess.model.Move;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ChessPiece implements Piece {
  public static boolean isEmpty(Piece piece) {
    return piece == null;
  }
  public static boolean isType(Piece piece, PieceType type) {
    return piece.getType() == type;
  }
  public static boolean isColor(Piece piece, PieceColor color) {
    return piece.getColor() == color;
  }

  private static int nextId = 0;
  private final int id;

  protected final PieceColor color;
  protected final PieceType type;
  protected int position;
  protected List<Move> pseudoLegalMoves;
  protected List<Integer> attackingPositions;

  public ChessPiece(PieceColor color, PieceType type, int position) {
    this.id = nextId++;
    this.color = color;
    this.type = type;
    this.position = position;
    this.pseudoLegalMoves = new ArrayList<>();
    this.attackingPositions = new ArrayList<>();
  }

  @Override
  public List<Integer> getAttackingPositions() {
    return attackingPositions;
  }

  @Override
  public PieceColor getColor() {
    return color;
  }

  @Override
  public PieceType getType() {
    return type;
  }

  @Override
  public int getPosition() {
    return position;
  }

  @Override
  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return PieceLookup.getPieceSymbol(this.color, this.type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Piece other = (Piece) obj;
    return
        this.position == other.getPosition() &&
        this.color == other.getColor() &&
        this.type == other.getType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
