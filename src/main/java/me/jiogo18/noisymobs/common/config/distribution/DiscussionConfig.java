package me.jiogo18.noisymobs.common.config.distribution;

import net.minecraftforge.common.ForgeConfigSpec;

public class DiscussionConfig implements IConfig {
    public final ForgeConfigSpec.BooleanValue useMinDuration;
    public final ForgeConfigSpec.DoubleValue probabilityToKeepDiscussion;
    public final ForgeConfigSpec.IntValue maximumDuration;
    public static final boolean DEFAULT_USE_MIN_DURATION = false;
    public static final double DEFAULT_PROBABILITY_TO_KEEP = 0.75;
    public static final int DEFAULT_MAXIMUM_DURATION = 1000;

    public DiscussionConfig(ForgeConfigSpec.Builder builder) {
        builder.push("discussion");
        this.useMinDuration = builder
                .comment("Keep the default minimum duration (typically 80 ticks depending on entity)")
                .translation("configured.config.client.sound_engine.discussion.use_min_duration")
                .define("useMinDuration", DEFAULT_USE_MIN_DURATION);
        this.probabilityToKeepDiscussion = builder
                .comment("The probability to keep the discussion going")
                .translation("configured.config.client.sound_engine.discussion.probability_to_keep")
                .defineInRange("probabilityToKeepDiscussion", DEFAULT_PROBABILITY_TO_KEEP, 0, 1);
        this.maximumDuration = builder
                .comment("The maximum duration between two sounds")
                .translation("configured.config.client.sound_engine.discussion.maximum_duration")
                .defineInRange("maximumDuration", DEFAULT_MAXIMUM_DURATION, 100, 1000000);
        builder.pop();
    }

    @Override
    public void reset() {
        this.useMinDuration.set(DEFAULT_USE_MIN_DURATION);
        this.probabilityToKeepDiscussion.set(DEFAULT_PROBABILITY_TO_KEEP);
        this.maximumDuration.set(DEFAULT_MAXIMUM_DURATION);
    }
}
