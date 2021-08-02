package net.minecraft.gametest.framework;

import javax.annotation.Nullable;

class GameTestHarnessEvent {

    @Nullable
    public final Long expectedDelay;
    public final Runnable assertion;

    private GameTestHarnessEvent(@Nullable Long olong, Runnable runnable) {
        this.expectedDelay = olong;
        this.assertion = runnable;
    }

    static GameTestHarnessEvent a(Runnable runnable) {
        return new GameTestHarnessEvent((Long) null, runnable);
    }

    static GameTestHarnessEvent a(long i, Runnable runnable) {
        return new GameTestHarnessEvent(i, runnable);
    }
}
