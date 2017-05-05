package com.tenshun.cache.generators;

/**
 * 04.05.2017.
 */
public class ScrambledZipfianGenerator extends IntegerGenerator {
    private static final long FNV_offset_basis_64 = 0xCBF29CE484222325L;
    private static final long FNV_prime_64 = 1099511628211L;

    public static final double ZETAN = 26.46902820178302;
    public static final double USED_ZIPFIAN_CONSTANT = 0.99;
    public static final long ITEM_COUNT = 10000000000L;

    ZipfianGenerator gen;
    long _min, _max, _itemcount;

    /******************************* Constructors **************************************/

    /**
     * Create a zipfian generator for the specified number of items.
     *
     * @param _items The number of items in the distribution.
     */
    public ScrambledZipfianGenerator(long _items) {
        this(0, _items - 1);
    }

    /**
     * Create a zipfian generator for items between min and max.
     *
     * @param _min The smallest integer to generate in the sequence.
     * @param _max The largest integer to generate in the sequence.
     */
    public ScrambledZipfianGenerator(long _min, long _max) {
        this(_min, _max, ZipfianGenerator.ZIPFIAN_CONSTANT);
    }

    /**
     * Create a zipfian generator for the specified number of items using the
     * specified zipfian constant.
     *
     * @param _items The number of items in the distribution.
     * @param _zipfianconstant The zipfian constant to use.
     */
    // not supported, as the value of zeta depends on the zipfian constant, and
    // we have only precomputed zeta for one zipfian constant
    //public ScrambledZipfianGenerator(long _items, double _zipfianconstant) {
    //  this(0,_items-1,_zipfianconstant);
    //}

    /**
     * Create a zipfian generator for items between min and max (inclusive) for
     * the specified zipfian constant. If you use a zipfian constant other than
     * 0.99, this will take a long time to complete because we need to recompute
     * zeta.
     *
     * @param min              The smallest integer to generate in the sequence.
     * @param max              The largest integer to generate in the sequence.
     * @param _zipfianconstant The zipfian constant to use.
     */
    public ScrambledZipfianGenerator(long min, long max, double _zipfianconstant) {
        _min = min;
        _max = max;
        _itemcount = _max - _min + 1;
        if (_zipfianconstant == USED_ZIPFIAN_CONSTANT) {
            gen = new ZipfianGenerator(0, ITEM_COUNT, _zipfianconstant, ZETAN);
        } else {
            gen = new ZipfianGenerator(0, ITEM_COUNT, _zipfianconstant);
        }
    }

    /**************************************************************************************************/

    /**
     * Return the next int in the sequence.
     */
    @Override
    public int nextInt() {
        return (int) nextLong();
    }

    /**
     * Return the next long in the sequence.
     */
    public long nextLong() {
        long ret = gen.nextLong();
        ret = _min + FNVhash64(ret) % _itemcount;
        setLastInt((int) ret);
        return ret;
    }

    /**
     * 64 bit FNV hash. Produces more "random" hashes than (say)
     * String.hashCode().
     *
     * @param val The value to hash.
     * @return The hash value
     */
    private static long FNVhash64(long val) {
        // from http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash
        long hashval = FNV_offset_basis_64;

        for (int i = 0; i < 8; i++) {
            long octet = val & 0x00ff;
            val = val >> 8;

            hashval = hashval ^ octet;
            hashval = hashval * FNV_prime_64;
            // hashval = hashval ^ octet;
        }
        return Math.abs(hashval);
    }
}