package com.example.demo.stream.trigger;

import lombok.Getter;

@Getter
public enum TriggerResult {
    /**
     * No action is taken on the window.
     */
    CONTINUE(false, false),

    /**
     * {@code FIRE_AND_PURGE} evaluates the window function and emits the window
     * result.
     */
    FIRE_AND_PURGE(true, true),

    /**
     * On {@code FIRE}, the window is evaluated and results are emitted.
     * The window is not purged, though, all elements are retained.
     */
    FIRE(true, false),

    /**
     * All elements in the window are cleared and the window is discarded,
     * without evaluating the window function or emitting any elements.
     */
    PURGE(false, true);

    private final boolean fire;
    private final boolean purge;

    TriggerResult(boolean fire, boolean purge) {
        this.fire = fire;
        this.purge = purge;
    }

}
