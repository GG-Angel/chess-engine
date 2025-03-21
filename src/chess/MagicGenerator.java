package chess;

import static chess.Board.bitboardToString;

import java.util.Arrays;
import java.util.Random;

/**
 * <a href="https://www.chessprogramming.org/Looking_for_Magics">
 *   Reference for Generating Magic Numbers
 * </a>
 */
public class MagicGenerator {
  // magic numbers generated
  private static final long[] ROOK_MAGICS = {
      0x2012100088680c08L,
      0x400004c0004a20L,
      0x3502280002010080L,
      0x431800000205021L,
      0x8080060400000000L,
      0x4400100209002cc4L,
      0x1000200208094084L,
      0x80403100000801L,
      0x12000300000020L,
      0x114000060020L,
      0x1410040240050000L,
      0x44200850281000L,
      0x41110280100a02L,
      0x102204041c000L,
      0x8011000202020080L,
      0x580802008400100L,
      0x4000200902000004L,
      0x111014c00003140L,
      0x24a4e04081500080L,
      0x2810050110000100L,
      0x42220000040001L,
      0x328500024a6d009L,
      0x910604020000100L,
      0x8004400000b8L,
      0x20010000641c0c48L,
      0x24b000002210000L,
      0x520201002049000L,
      0x41001890028080L,
      0x84050020000204c0L,
      0x29000c4102000230L,
      0x144300006020L,
      0x10800888480446ccL,
      0x20ea0004801af4L,
      0x204b090004408L,
      0x150040000200809L,
      0x1190020c20400002L,
      0x6034804002100L,
      0x40004000000d8800L,
      0x1020300008000200L,
      0x1001000024b0008cL,
      0x9a0000410002e40L,
      0x4000820620000c28L,
      0x2850000008009204L,
      0xc18030890050082L,
      0x8408000080830040L,
      0x840060040020384L,
      0x23001288300a688L,
      0x16004920000L,
      0x1080a0824400100L,
      0x42181c0443029884L,
      0x20011411058800L,
      0x5000228102040800L,
      0x80086040858L,
      0xc000100000102080L,
      0x140282014000L,
      0x48800520001L,
      0x4010098000021L,
      0x1c10b2144004400L,
      0x1045008010660000L,
      0xc001000400421800L,
      0x8030100843042000L,
      0x100001841c40800aL,
      0x2081400000408001L,
      0x900440022001c018L,
  };

  private static final long[] BISHOP_MAGICS = {
      0x8400218120024L,
      0x232002120800013L,
      0x80c084000088000L,
      0x62c005810100020L,
      0x8214104018410120L,
      0x9012804900202880L,
      0x200094100904018cL,
      0x10322190000808L,
      0x9000208a02000028L,
      0xa028500148b08804L,
      0xcf1002020040L,
      0x1004129220100410L,
      0x400a14020412040L,
      0x408600000a005L,
      0x4001422000025081L,
      0x2265010401040103L,
      0xc08885480084010L,
      0x10084d20000440L,
      0xa84009000210440L,
      0x88000011800444L,
      0x1800800810104021L,
      0x8044000030000000L,
      0x8420a44004004229L,
      0x40008020880144a0L,
      0x8020c00081124a20L,
      0x400200a441140050L,
      0x1400400020409040L,
      0x4060008008c112L,
      0x4024a022288203L,
      0x2041110808200080L,
      0x1068004a1514800L,
      0x1200001024108L,
      0x8083080108218L,
      0x1802500842400600L,
      0x182420d0a14001L,
      0x10100c4000122808L,
      0x1000080c021004L,
      0x1200302a011d000L,
      0x1000620310110L,
      0x2041000481000a40L,
      0x2241c040080008a0L,
      0x802001e00000380L,
      0x4884200010514L,
      0x80020200814010L,
      0x2000180400804a9L,
      0x8106c4049a98014L,
      0x4200c0120214001L,
      0x14082029216100L,
      0x201040033400612L,
      0x1111292008002010L,
      0x11140a0090008L,
      0x18004130042202L,
      0x81848009004a4000L,
      0x8002108280010820L,
      0x31400a00842008a0L,
      0x4a84200001c285L,
      0x408200900042101L,
      0xc0084500c0700810L,
      0x40400500e00052L,
      0x4100014402100482L,
      0x100000258c48040cL,
      0xa32400a080830041L,
      0x8801204080890088L,
      0x1008901408002224L,
  };

  // important tables
  private static final int[] BitTable = {
      63, 30, 3, 32, 25, 41, 22, 33, 15, 50, 42, 13, 11, 53, 19, 34, 61, 29, 2,
      51, 21, 43, 45, 10, 18, 47, 1, 54, 9, 57, 0, 35, 62, 31, 40, 4, 49, 5, 52,
      26, 60, 6, 23, 44, 46, 27, 56, 16, 7, 39, 48, 24, 59, 14, 12, 55, 38, 28,
      58, 20, 37, 17, 36, 8
  };

  private static final int[] RookBits = {
      12, 11, 11, 11, 11, 11, 11, 12,
      11, 10, 10, 10, 10, 10, 10, 11,
      11, 10, 10, 10, 10, 10, 10, 11,
      11, 10, 10, 10, 10, 10, 10, 11,
      11, 10, 10, 10, 10, 10, 10, 11,
      11, 10, 10, 10, 10, 10, 10, 11,
      11, 10, 10, 10, 10, 10, 10, 11,
      12, 11, 11, 11, 11, 11, 11, 12
  };

  private static final int[] BishopBits = {
      6, 5, 5, 5, 5, 5, 5, 6,
      5, 5, 5, 5, 5, 5, 5, 5,
      5, 5, 7, 7, 7, 7, 5, 5,
      5, 5, 7, 9, 9, 7, 5, 5,
      5, 5, 7, 9, 9, 7, 5, 5,
      5, 5, 7, 7, 7, 7, 5, 5,
      5, 5, 5, 5, 5, 5, 5, 5,
      6, 5, 5, 5, 5, 5, 5, 6
  };

  // masks
  private static final long[] ROOK_MASKS = new long[64];
  private static final long[] BISHOP_MASKS = new long[64];

  // attacks
  private static final long[][] ROOK_ATTACKS = new long[64][4096];
  private static final long[][] BISHOP_ATTACKS = new long[64][512];

  // initialize tables
  static {
    initializeMaskTables();
    initializeAttackTables();
  }

  private static void initializeMaskTables() {
    for (int square = 0; square < 64; square++) {
      ROOK_MASKS[square] = generateRookMask(square);
      BISHOP_MASKS[square] = generateBishopMask(square);
    }
  }

  private static void initializeAttackTables() {
    for (int square = 0; square < 64; square++) {
      long rookMask = generateRookMask(square);
      long bishopMask = generateBishopMask(square);
      int rookBits = RookBits[square];
      int bishopBits = BishopBits[square];

      // initialize rook attacks
      for (int i = 0; i < (1 << rookBits); i++) {
        long blockers = convertIndexToBitboard(i, rookBits, rookMask);
        int index = transform(blockers, ROOK_MAGICS[square], rookBits);
        ROOK_ATTACKS[square][index] = generateRookAttacks(square, blockers);
      }

      // initialize bishop attacks
      for (int i = 0; i < (1 << bishopBits); i++) {
        long blockers = convertIndexToBitboard(i, bishopBits, bishopMask);
        int index = transform(blockers, BISHOP_MAGICS[square], bishopBits);
        BISHOP_ATTACKS[square][index] = generateBishopAttacks(square, blockers);

      }
    }
  }

  // getters for sliding move lookups in the chess engine
  public static long getRookAttacks(int square, long blockers) {
    long mask = ROOK_MASKS[square];
    long magic = ROOK_MAGICS[square];
    int index = transform(blockers & mask, magic, RookBits[square]);
    return ROOK_ATTACKS[square][index];
  }

  public static long getBishopAttacks(int square, long blockers) {
    long mask = BISHOP_MASKS[square];
    long magic = BISHOP_MAGICS[square];
    int index = transform(blockers & mask, magic, BishopBits[square]);
    return BISHOP_ATTACKS[square][index];
  }

  /** -------------------------------
   *  Magic Bitboard Generation Logic
   *  ------------------------------- **/

  private static final Random random = new Random();

  private static long generateRandom64Bit() {
    long u1, u2, u3, u4;
    u1 = (long) random.nextInt() & 0xFFFF;
    u2 = (long) random.nextInt() & 0xFFFF;
    u3 = (long) random.nextInt() & 0xFFFF;
    u4 = (long) random.nextInt() & 0xFFFF;
    return u1 | (u2 << 16) | (u3 << 32) | (u4 << 48);
  }

  private static long generateRandomMagicNumber() {
    return generateRandom64Bit() & generateRandom64Bit() & generateRandom64Bit();
  }

  private static int countSetBits(long bitboard) {
    int count = 0;
    while (bitboard != 0) {
      count++;
      bitboard &= bitboard - 1;
    }
    return count;
  }

  private static int popFirstSetBit(long[] bitboardRef) {
    long b = bitboardRef[0] ^ (bitboardRef[0] - 1);
    int fold = (int) ((b & 0xFFFFFFFFL) ^ (b >>> 32));
    bitboardRef[0] &= (bitboardRef[0] - 1);
    return BitTable[(fold * 0x783a9b23) >>> 26];
  }

  private static long convertIndexToBitboard(int index, int bits, long mask) {
    long bitboard = 0L;
    for (int i = 0; i < bits; i++) {
      int bitPosition = popFirstSetBit(new long[]{mask});
      if ((index & (1 << i)) != 0) {
        bitboard |= (1L << bitPosition);
      }
    }
    return bitboard;
  }

  private static long generateRookMask(int square) {
    long mask = 0L;
    int rank = square / 8;
    int file = square % 8;
    int r, f;

    for (r = rank + 1; r <= 6; r++) {
      mask |= (1L << (file + (r * 8)));
    }

    for (r = rank - 1; r >= 1; r--) {
      mask |= (1L << (file + (r * 8)));
    }

    for (f = file + 1; f <= 6; f++) {
      mask |= (1L << (f + (rank * 8)));
    }

    for (f = file - 1; f >= 1; f--) {
      mask |= (1L << (f + (rank * 8)));
    }

    return mask;
  }

  private static long generateBishopMask(int square) {
    long mask = 0L;
    int rank = square / 8, file = square % 8;

    for (int r = rank + 1, f = file + 1; r <= 6 && f <= 6; r++, f++) {
      mask |= (1L << (f + (r * 8)));
    }

    for (int r = rank + 1, f = file - 1; r <= 6 && f >= 1; r++, f--) {
      mask |= (1L << (f + (r * 8)));
    }

    for (int r = rank - 1, f = file + 1; r >= 1 && f <= 6; r--, f++) {
      mask |= (1L << (f + (r * 8)));
    }

    for (int r = rank - 1, f = file - 1; r >= 1 && f >= 1; r--, f--) {
      mask |= (1L << (f + (r * 8)));
    }

    return mask;
  }

  private static long generateRookAttacks(int square, long blockers) {
    long attacks = 0L;
    int rank = square / 8, file = square % 8;

    for (int r = rank + 1; r <= 7; r++) {
      long attackPosition = 1L << (file + (r * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    for (int r = rank - 1; r >= 0; r--) {
      long attackPosition = 1L << (file + (r * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    for (int f = file + 1; f <= 7; f++) {
      long attackPosition = 1L << (f + (rank * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    for (int f = file - 1; f >= 0; f--) {
      long attackPosition = 1L << (f + (rank * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    return attacks;
  }

  private static long generateBishopAttacks(int square, long blockers) {
    long attacks = 0L;
    int rank = square / 8, file = square % 8;

    for (int r = rank + 1, f = file + 1; r <= 7 && f <= 7; r++, f++) {
      long attackPosition = 1L << (f + (r * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    for (int r = rank + 1, f = file - 1; r <= 7 && f >= 0; r++, f--) {
      long attackPosition = 1L << (f + (r * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    for (int r = rank - 1, f = file + 1; r >= 0 && f <= 7; r--, f++) {
      long attackPosition = 1L << (f + (r * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    for (int r = rank - 1, f = file - 1; r >= 0 && f >= 0; r--, f--) {
      long attackPosition = 1L << (f + (r * 8));
      attacks |= attackPosition;
      if ((blockers & attackPosition) != 0) break;
    }

    return attacks;
  }

//  private static int transform(long bitboard, long magic, int bits) {
//    long product1 = (bitboard & 0xFFFFFFFFL) * (magic & 0xFFFFFFFFL);
//    long product2 = ((bitboard >>> 32) * (magic >>> 32));
//    long combined = (product1 ^ product2) >>> (32 - bits);
//    int index = (int) combined & ((1 << bits) - 1);
//
//    System.out.println("Transform Debug:");
//    System.out.println("Bitboard: " + Long.toBinaryString(bitboard));
//    System.out.println("Magic: " + Long.toBinaryString(magic));
//    System.out.println("Product1: " + Long.toBinaryString(product1));
//    System.out.println("Product2: " + Long.toBinaryString(product2));
//    System.out.println("Combined: " + Long.toBinaryString(combined));
//    System.out.println("Index: " + index);
//
//    return index;
//  }

  private static int transform(long bitboard, long magic, int bits) {
    long product = bitboard * magic;
    return (int) (product >>> (64 - bits));
  }

  private static long findMagic(int square, int numberOfBitsInMagic, boolean isBishop) throws IllegalStateException {
    long attackMask = isBishop ? generateBishopMask(square) : generateRookMask(square);
    int numberOfBitsInMask = countSetBits(attackMask);
    int maxIndex = 1 << numberOfBitsInMagic;

    long[] blockerConfigurations = new long[1 << numberOfBitsInMask];
    long[] attackSets = new long[1 << numberOfBitsInMask];

    long[] usedAttackSets = new long[maxIndex];
    boolean isMagicInvalid;

    // generate all possible blocker configurations and their corresponding attack sets
    for (int i = 0; i < (1 << numberOfBitsInMask); i++) {
      blockerConfigurations[i] = convertIndexToBitboard(i, numberOfBitsInMask, attackMask);
      attackSets[i] = isBishop ? generateBishopAttacks(square, blockerConfigurations[i]) : generateRookAttacks(square, blockerConfigurations[i]);
    }

    // try to find a valid magic number
    for (int attempt = 0; attempt < 100000000; attempt++) {
      long magicNumber = generateRandomMagicNumber();

      // skip magic numbers with too few set bits in the high byte
      if (countSetBits((attackMask * magicNumber) & 0xFF00000000000000L) < 6) continue;

      // reset used array for this attempt
      Arrays.fill(usedAttackSets, 0L);

      isMagicInvalid = false;
      for (int i = 0; !isMagicInvalid && i < (1 << numberOfBitsInMask); i++) {
        int index = transform(blockerConfigurations[i], magicNumber, numberOfBitsInMagic);

        // check if the index is out of bounds
        if (index >= maxIndex) {
          isMagicInvalid = true;
          break;
        }

        // if the index is unused, store the attack set
        if (usedAttackSets[index] == 0L) {
          usedAttackSets[index] = attackSets[i];
        }

        // if the index is already used, check for consistency
        else if (usedAttackSets[index] != attackSets[i]) {
          isMagicInvalid = true;
        }
      }

      if (!isMagicInvalid) {
        return magicNumber;
      }
    }

    throw new IllegalStateException("Failed to find magic number.");
  }

  public static void testGetAttackBitboard() {
    // Example: Test rook attacks on square e4 (square 28)
    int square = 28; // e4
    long blockers = 0x1000000000L; // Blockers on e5 and e6
    long rookAttacks = getRookAttacks(square, blockers);
    System.out.println("Rook Attacks from e4 with blockers on e5 and e6:");
    System.out.println(bitboardToString(rookAttacks));

    // Example: Test bishop attacks on square c4 (square 26)
    square = 26; // c4
    blockers = 0x800000000L; // Blockers on b5 and d5
    long bishopAttacks = getBishopAttacks(square, blockers);
    System.out.println("Bishop Attacks from c4 with blockers on b5 and d5:");
    System.out.println(bitboardToString(bishopAttacks));
  }

  public static void printMasks() {
    System.out.println("Rook Mask for e4 (square 28):");
    System.out.println(bitboardToString(ROOK_MASKS[28]));

    System.out.println("Bishop Mask for c4 (square 26):");
    System.out.println(bitboardToString(BISHOP_MASKS[26]));
  }

  public static void printAttackTableEntry(int square, long blockers, boolean isRook) {
    long mask = isRook ? ROOK_MASKS[square] : BISHOP_MASKS[square];
    long magic = isRook ? ROOK_MAGICS[square] : BISHOP_MAGICS[square];
    int bits = isRook ? RookBits[square] : BishopBits[square];
    int index = transform(blockers & mask, magic, bits);

    long attacks = isRook ? ROOK_ATTACKS[square][index] : BISHOP_ATTACKS[square][index];
    System.out.println("Attack Bitboard for square " + square + " with blockers " + Long.toBinaryString(blockers) + ":");
    System.out.println(bitboardToString(attacks));
  }

  // try generating some magic numbers yourself :D
  public static void main(String[] args) {
    // Regenerate magic numbers with the fixed transform function
    System.out.println("private static final long[] ROOK_MAGICS = {");
    for (int square = 0; square < 64; square++) {
      System.out.printf("  0x%xL,\n", findMagic(square, RookBits[square], false));
    }
    System.out.println("};\n");

    System.out.println("private static final long[] BISHOP_MAGICS = {");
    for (int square = 0; square < 64; square++) {
      System.out.printf("  0x%xL,\n", findMagic(square, BishopBits[square], true));
    }
    System.out.println("};");

    // Reinitialize attack tables with new magics
    initializeAttackTables();

    // Test the attack bitboards
    testGetAttackBitboard();
  }
}
