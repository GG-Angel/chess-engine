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
      0x2008011010000042L,
      0x35a80004d02001L,
      0x112801a12000028L,
      0x4002700100404L,
      0x51404108400200L,
      0x400002210000000L,
      0x20054082000000L,
      0x8050065040050000L,
      0x202003080011000L,
      0x481000404200901L,
      0xa88000a4c30c000L,
      0x6445000000200411L,
      0x10420004100a140L,
      0x40840c0100c04a00L,
      0x426200400410a015L,
      0x400289001202180L,
      0x4000020502c11420L,
      0xc000181000038020L,
      0x8186901a20040L,
      0x808000000904210L,
      0x20009a000L,
      0x9180000060240L,
      0x80c00000a4044104L,
      0x402200200001800L,
      0x221800010420002L,
      0xc60050006100002L,
      0x9021000400500080L,
      0x8010048200L,
      0x9800001140104L,
      0x4020224000080d08L,
      0xc400001000100810L,
      0x1004000286000080L,
      0x8000200204100L,
      0xa220088000054060L,
      0x22000200c0080010L,
      0x1800000040802805L,
      0x500100800adL,
      0x10144000c7002180L,
      0x8a003800059001L,
      0x580008508a000104L,
      0x1000408400048000L,
      0x4101281040000L,
      0x908000000c003L,
      0x420200004121008L,
      0x50110210040014L,
      0x2000042820442050L,
      0x430008308200920L,
      0x10404a0000048040L,
      0x4000020011002050L,
      0x1081010809490050L,
      0x44d405a000028020L,
      0x5201420880004010L,
      0x80040081030cL,
      0x24209080e0404800L,
      0x222200400000800L,
      0x20111202008L,
      0x100000010110040cL,
      0x901081100054000L,
      0x202800122080120L,
      0x200010014120544L,
      0x80002901101003L,
      0x902000041198060L,
      0xc0010000000125L,
      0x4008808842304000L,
  };

  private static final long[] BISHOP_MAGICS = {
      0x418881380a0000L,
      0x10a00090a0810L,
      0x8060101088240012L,
      0x2036020204341240L,
      0x804050200040500L,
      0x880c0182041810L,
      0x8000c4404042104L,
      0x244010048620000L,
      0x2002a0800008810L,
      0x80010100b0071000L,
      0xc0040042008c000L,
      0x8000d200005a1843L,
      0x220980000920L,
      0x20a1009081040400L,
      0x8460181210L,
      0x8000080820301980L,
      0x440400228024b800L,
      0x48140001202520L,
      0x108a880000013080L,
      0x10000e5008c0220L,
      0x22224000820000L,
      0x4323102ac80056L,
      0xc004000149088000L,
      0x401204014040a20cL,
      0x14c01001428400L,
      0x40041210a05028aL,
      0x8000000008224448L,
      0x944102000080008L,
      0xa4140000021008c7L,
      0x220604020010L,
      0x5100000411188251L,
      0x10020c408020080L,
      0x350050c02a0040L,
      0x16327801060c188L,
      0x805d000100088400L,
      0x9000000002018c00L,
      0x4301000428122000L,
      0x240040500038444L,
      0x2204805423012221L,
      0x10201004b0022010L,
      0x80039041010012L,
      0x9c00015840042800L,
      0x4a1000840801080L,
      0x120010011002861L,
      0x510004120008210L,
      0x2803800102830010L,
      0x60040102200a0024L,
      0x40480081083L,
      0x80411004064d112L,
      0x9640808890208040L,
      0x300018005212642L,
      0x40000104029801L,
      0x2408104008085618L,
      0x10204080020f41L,
      0x2848000000133101L,
      0x8060000240054808L,
      0x2100286042922042L,
      0x810048200110L,
      0xa002001110100421L,
      0xd00a000400c0830L,
      0x4009001000a09360L,
      0x9003520484100110L,
      0x8081400103401900L,
      0x8808012000620410L,
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

      for (int i = 0; i < (1 << rookBits); i++) {
        long blockers = convertIndexToBitboard(i, rookBits, rookMask);
        int index = transform(blockers, ROOK_MAGICS[square], rookBits);
        ROOK_ATTACKS[square][index] = generateRookAttacks(square, blockers);
      }

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

  private static int transform(long bitboard, long magic, int bits) {
    long product1 = (bitboard & 0xFFFFFFFFL) * (magic & 0xFFFFFFFFL);
    long product2 = ((bitboard >>> 32) * (magic >>> 32));
    long combined = (product1 ^ product2) >>> (32 - bits);
    return (int) combined & ((1 << bits) - 1);
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

    System.out.println("------------------------------");

    printMasks();

    System.out.println("------------------------------");

    // Test rook attacks from e4 with blockers on e5 and e6
    int square = 28; // e4
    long blockers = 0x1000000000L; // Blockers on e5 and e6
    printAttackTableEntry(square, blockers, true);

    // Test bishop attacks from c4 with blockers on b5 and d5
    square = 26; // c4
    blockers = 0x800000000L; // Blockers on b5 and d5
    printAttackTableEntry(square, blockers, false);
  }
}
