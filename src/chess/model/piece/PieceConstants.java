package chess.model.piece;

public class PieceConstants {
  // 0:4 are straights: up, left, right, down | 4:8 are diagonals: nw, ne, sw, se
  public final static int[] DIRECTION_OFFSETS = new int[] { -8, -1, 1, 8, -9, -7, 7, 9 };

  // access by position -> direction offset
  public final static int[][] NUM_SQUARES_FROM_EDGE = new int[][] {
      {0, 0, 7, 7, 0, 0, 0, 7},
      {0, 1, 6, 7, 0, 0, 1, 6},
      {0, 2, 5, 7, 0, 0, 2, 5},
      {0, 3, 4, 7, 0, 0, 3, 4},
      {0, 4, 3, 7, 0, 0, 4, 3},
      {0, 5, 2, 7, 0, 0, 5, 2},
      {0, 6, 1, 7, 0, 0, 6, 1},
      {0, 7, 0, 7, 0, 0, 7, 0},
      {1, 0, 7, 6, 0, 1, 0, 6},
      {1, 1, 6, 6, 1, 1, 1, 6},
      {1, 2, 5, 6, 1, 1, 2, 5},
      {1, 3, 4, 6, 1, 1, 3, 4},
      {1, 4, 3, 6, 1, 1, 4, 3},
      {1, 5, 2, 6, 1, 1, 5, 2},
      {1, 6, 1, 6, 1, 1, 6, 1},
      {1, 7, 0, 6, 1, 0, 6, 0},
      {2, 0, 7, 5, 0, 2, 0, 5},
      {2, 1, 6, 5, 1, 2, 1, 5},
      {2, 2, 5, 5, 2, 2, 2, 5},
      {2, 3, 4, 5, 2, 2, 3, 4},
      {2, 4, 3, 5, 2, 2, 4, 3},
      {2, 5, 2, 5, 2, 2, 5, 2},
      {2, 6, 1, 5, 2, 1, 5, 1},
      {2, 7, 0, 5, 2, 0, 5, 0},
      {3, 0, 7, 4, 0, 3, 0, 4},
      {3, 1, 6, 4, 1, 3, 1, 4},
      {3, 2, 5, 4, 2, 3, 2, 4},
      {3, 3, 4, 4, 3, 3, 3, 4},
      {3, 4, 3, 4, 3, 3, 4, 3},
      {3, 5, 2, 4, 3, 2, 4, 2},
      {3, 6, 1, 4, 3, 1, 4, 1},
      {3, 7, 0, 4, 3, 0, 4, 0},
      {4, 0, 7, 3, 0, 4, 0, 3},
      {4, 1, 6, 3, 1, 4, 1, 3},
      {4, 2, 5, 3, 2, 4, 2, 3},
      {4, 3, 4, 3, 3, 4, 3, 3},
      {4, 4, 3, 3, 4, 3, 3, 3},
      {4, 5, 2, 3, 4, 2, 3, 2},
      {4, 6, 1, 3, 4, 1, 3, 1},
      {4, 7, 0, 3, 4, 0, 3, 0},
      {5, 0, 7, 2, 0, 5, 0, 2},
      {5, 1, 6, 2, 1, 5, 1, 2},
      {5, 2, 5, 2, 2, 5, 2, 2},
      {5, 3, 4, 2, 3, 4, 2, 2},
      {5, 4, 3, 2, 4, 3, 2, 2},
      {5, 5, 2, 2, 5, 2, 2, 2},
      {5, 6, 1, 2, 5, 1, 2, 1},
      {5, 7, 0, 2, 5, 0, 2, 0},
      {6, 0, 7, 1, 0, 6, 0, 1},
      {6, 1, 6, 1, 1, 6, 1, 1},
      {6, 2, 5, 1, 2, 5, 1, 1},
      {6, 3, 4, 1, 3, 4, 1, 1},
      {6, 4, 3, 1, 4, 3, 1, 1},
      {6, 5, 2, 1, 5, 2, 1, 1},
      {6, 6, 1, 1, 6, 1, 1, 1},
      {6, 7, 0, 1, 6, 0, 1, 0},
      {7, 0, 7, 0, 0, 7, 0, 0},
      {7, 1, 6, 0, 1, 6, 0, 0},
      {7, 2, 5, 0, 2, 5, 0, 0},
      {7, 3, 4, 0, 3, 4, 0, 0},
      {7, 4, 3, 0, 4, 3, 0, 0},
      {7, 5, 2, 0, 5, 2, 0, 0},
      {7, 6, 1, 0, 6, 1, 0, 0},
      {7, 7, 0, 0, 7, 0, 0, 0},
  };
}
