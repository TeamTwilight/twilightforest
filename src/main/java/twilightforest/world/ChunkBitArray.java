package twilightforest.world;

public class ChunkBitArray {
    private static final int CHUNK_SIZE = (16 * 256 * 16);
    private static final int BITS_PER_WORD = 6;

    private final long[] words = new long[CHUNK_SIZE >> 3];

    public void set(int index) {
        this.words[index >> BITS_PER_WORD] |= (1L << index);
    }

    public void set(int index, boolean value) {
        if (value) {
            this.set(index);
        } else {
            this.clear(index);
        }
    }

    public void clear(int index) {
        this.words[index >> BITS_PER_WORD] &= ~(1L << index);
    }

    public boolean get(int index) {
        return (this.words[index >> BITS_PER_WORD] & (1L << index)) != 0;
    }
}
