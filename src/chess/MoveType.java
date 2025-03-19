package chess;

public enum MoveType {
  QUIET                (0),
  DOUBLE_PAWN_PUSH     (1),
  KING_CASTLE          (2),
  QUEEN_CASTLE         (3),
  CAPTURE              (4),
  EP_CAPTURE           (5),
  KNIGHT_PROMO         (8),
  BISHOP_PROMO         (9),
  ROOK_PROMO           (10),
  QUEEN_PROMO          (11),
  KNIGHT_PROMO_CAPTURE (12),
  BISHOP_PROMO_CAPTURE (13),
  ROOK_PROMO_CAPTURE   (14),
  QUEEN_PROMO_CAPTURE  (15);

  public static final MoveType[] Promotions = {
      KNIGHT_PROMO, BISHOP_PROMO, ROOK_PROMO, QUEEN_PROMO,
  };

  public static final MoveType[] PromotionCaptures = {
      KNIGHT_PROMO_CAPTURE, BISHOP_PROMO_CAPTURE, ROOK_PROMO_CAPTURE, QUEEN_PROMO_CAPTURE
  };

  private final byte code;

  MoveType(int code) {
    this.code = (byte) code;
  }

  public byte code() {
    return code;
  }
}
