package me.jiogo18.noisymobs.common.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.jiogo18.noisymobs.common.config.ConfigManager;

public class SoundLimiterEngine {
    private static SoundLimiterEngine instance;
    private final SoundLimiter ambientSoundLimiter;
    private final SoundLimiter stepSoundLimiter;
    private static final int CYCLE_DURATION = 2; // seconds

    public static SoundLimiterEngine getInstance() {
        if (instance == null) {
            instance = new SoundLimiterEngine();
            setEnabled(ConfigManager.isSoundLimiterEnabled());
        }
        return instance;
    }

    private SoundLimiterEngine() {
        ambientSoundLimiter = new SoundLimiter(5, CYCLE_DURATION);
        stepSoundLimiter = new SoundLimiter(4, CYCLE_DURATION);
        // Reset the sound count limit every second
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> resetSoundLimit(), 0, CYCLE_DURATION, TimeUnit.SECONDS);
    }

    private void resetSoundLimit() {
        ambientSoundLimiter.resetCycle();
        stepSoundLimiter.resetCycle();
    }

    public static SoundLimiter getAmbientSoundLimiter() {
        return getInstance().ambientSoundLimiter;
    }

    public static SoundLimiter getStepSoundLimiter() {
        return getInstance().stepSoundLimiter;
    }

    public static void setEnabled(boolean enabled) {
        if (instance == null)
            return;
        instance.ambientSoundLimiter.setEnabled(enabled);
        instance.stepSoundLimiter.setEnabled(enabled);
    }
}
