package com.example.demo.stream.windowing;

import java.util.concurrent.ThreadLocalRandom;

public enum WindowStagger {
    ALIGNED {
        @Override
        public long getStaggerOffset(long timestamp, long size) {
            return 0L;
        }
    },
    RANDOM {
        @Override
        public long getStaggerOffset(long timestamp, long size) {
            return (long) (ThreadLocalRandom.current().nextDouble() * size);
        }
    },
    NATURAL {
        @Override
        public long getStaggerOffset(final long timestamp, final long size) {
            final long windowStart = TimeWindow.getWindowStartWithOffset(timestamp, 0, size);
            return Math.max(0, timestamp - windowStart);
        }
    };

    public abstract long getStaggerOffset(final long timestamp, final long size);
}
