package utilities;

import static utilities.Utils.to1D;

import java.util.Arrays;

public class SquaresFromEdgeTable {

  public static void main(String[] args) {
    int[][] board = new int[8][8];
    // Directions: right, left, down, up, and the four diagonals
    int[][] directionOffsets = new int[][] {
        {-1, 0}, {0, -1}, {0, 1}, {1, 0}, // up, left, right, down
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // nw, ne, sw, se
    };

    int[][] numSquaresFromEdge = new int[64][8]; // Array to store the results for each position

    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 8; x++) {
        for (int i = 0; i < 8; i++) {
          int[] offset = directionOffsets[i];
          int n = 0;
          while (true) {
            int toY = y + offset[0] * (n + 1);
            int toX = x + offset[1] * (n + 1);

            // Check if the new position is within bounds
            if (toY < 0 || toY >= 8 || toX < 0 || toX >= 8) {
              break;  // Stop when the position goes out of bounds
            }

            // Optionally, mark the visited squares with a flag in the board array
            board[toY][toX] = -100;  // Just for debugging purposes
            n++;
          }

          // Store the number of squares to the edge in the numSquaresFromEdge array
          numSquaresFromEdge[to1D(x, y)][i] = n;

        }
//        System.out.println(x + "," + y + ": " + Arrays.toString(numSquaresFromEdge[to1D(x, y)]));
        System.out.println(Arrays.toString(numSquaresFromEdge[to1D(x, y)]).replace("[", "{").replace("]", "}")+",");
      }
    }

//    System.out.println(Arrays.toString(numSquaresFromEdge[26 - 1]));
//    System.out.println(Arrays.toString(numSquaresFromEdge[14 - 1]));

//    1  2  3  4  5  6  7  8
//    9  10 11 12 13 14 15 16
//    17 18 19 20 21 22 23 24
//    25 26 27 28 29 30 31 32
//    33 34 35 36 37 38 39 40
//    41 42 43 44 45 46 47 48
//    49 50 51 52 53 54 55 56
//    57 58 59 60 61 62 63 64
//
//        -9 -8 -7
//        -1  .   1
//         7  8  9
//
//    26: 3, 1, 6, 4, 1, 3, 1, 4
//    14: 1, 5, 2, 6, 1, 1, 5, 2
//
//    Rook: -8 -1 1 8
//    Bishop: -9 -7 7 9

    // Print out the number of squares from the edge for the first position (0,0)
//    System.out.println(Arrays.toString(numSquaresFromEdge[0]));
  }
}
