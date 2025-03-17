package chess;

import static chess.Utilities.getRankAndFile;

import java.util.Objects;

public class Move {
  private final int from, to;
  private final Piece promotion;

  public Move(int from, int to) {
    this(from, to, null);
  }

  public Move(int from, int to, Piece promotion) {
    this.from = from;
    this.to = to;
    this.promotion = promotion;
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }

  public Piece getPromotion() {
    return Objects.requireNonNull(promotion, "Not a promotion move.");
  }

  @Override
  public String toString() {
    return String.format("%s%s", getRankAndFile(from), getRankAndFile(to));
  }
}
