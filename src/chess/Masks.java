package chess;

public class Masks {
  public static final long universe = 0xFFFFFFFFFFFFFFFFL;
  public static final long empty = 0L;

  public static final long rank8  = 0xFF00000000000000L;
  public static final long rank5  = 0xFF00000000L;
  public static final long rank4  = 0xFF000000L;
  public static final long rank1  = 0xFFL;
  public static final long fileA  = 0x8080808080808080L;
  public static final long fileAB = 0x4040404040404040L | fileA;
  public static final long fileGH = 0x0303030303030303L;
  public static final long fileH  = 0x0101010101010101L;

  public static final long knightMask = 0xa1100110aL;
}
