package ms.thesis.common.record;


import java.util.SplittableRandom;
import java.util.function.Supplier;

public class RandomRecordGenerator implements Supplier<ByteArrayRecord> {

    private static final int RECORD_SIZE = 1024; // 1kB
    private final SplittableRandom rng;

    public RandomRecordGenerator(long seed) {
        this.rng = new SplittableRandom(seed);
    }

    @Override
    public ByteArrayRecord get() {
        byte[] data = new byte[RECORD_SIZE];
        rng.nextBytes(data);
        return new ByteArrayRecord(data);
    }
}