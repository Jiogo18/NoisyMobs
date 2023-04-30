package me.jiogo18.noisymobs.common.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import me.jiogo18.noisymobs.NoisyMobsMod;
import me.jiogo18.noisymobs.common.config.distribution.*;
import me.jiogo18.noisymobs.common.engine.SoundEngine;
import me.jiogo18.noisymobs.common.engine.SoundLimiterEngine;
import me.jiogo18.noisymobs.common.engine.distribution.SoundDistribution;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager {
    private static final ConfigManager INSTANCE;
    private static final ForgeConfigSpec SPEC;
    private static final Path CONFIG_PATH = Paths.get("config", NoisyMobsMod.MODID + ".toml");

    // General
    private final ForgeConfigSpec.BooleanValue soundEngineEnabled;
    private final ForgeConfigSpec.EnumValue<SoundDistribution> soundDistribution;
    private final ForgeConfigSpec.DoubleValue durationModifierFactor;
    private final ForgeConfigSpec.BooleanValue soundLimiterEnabled;
    public static final boolean DEFAULT_SOUND_ENGINE_ENABLED = true;
    public static final SoundDistribution DEFAULT_SOUND_DISTRIBUTION = SoundDistribution.DISCUSSION;
    public static final double DEFAULT_DURATION_MODIFIER_FACTOR = 0.2;
    public static final boolean DEFAULT_SOUND_LIMITER_ENABLED = true;

    // Distributions
    public final VanillaConfig vanillaConfig;
    public final DiscussionConfig discussionConfig;

    static {
        Pair<ConfigManager, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigManager::new);
        INSTANCE = specPair.getLeft();
        SPEC = specPair.getRight();
        CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_PATH)
                .sync()
                .autoreload()
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        config.save();
        SPEC.setConfig(config);
    }

    private ConfigManager(ForgeConfigSpec.Builder builder) {
        this.soundEngineEnabled = builder.comment("Enables the sound engine")
                .translation("configured.config.client.sound_engine.enabled")
                .define("soundEngineEnabled", DEFAULT_SOUND_ENGINE_ENABLED);
        this.soundDistribution = builder.comment("The distribution of the sound engine")
                .translation("configured.config.client.sound_engine.distribution")
                .defineEnum("soundDistribution", DEFAULT_SOUND_DISTRIBUTION);
        this.durationModifierFactor = builder
                .comment(
                        "How much the number of entities arround should reduce the amount of noises. This increases the time between noises. (0 to disable)")
                .translation("configured.config.client.sound_engine.duration_modifier_factor")
                .defineInRange("durationModifierFactor", DEFAULT_DURATION_MODIFIER_FACTOR, 0, 10);
        this.soundLimiterEnabled = builder.comment("Enables the sound limiter")
                .translation("configured.config.client.sound_limiter.enabled")
                .define("soundLimiterEnabled", DEFAULT_SOUND_LIMITER_ENABLED);

        builder.push("customization");
        this.vanillaConfig = new VanillaConfig(builder);
        this.discussionConfig = new DiscussionConfig(builder);
        builder.pop();
    }

    public static ConfigManager getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("ConfigManager has not been initialized yet");
        return INSTANCE;
    }

    public static void save() {
        SPEC.save();
    }

    public static void reset() {
        getInstance().soundEngineEnabled.set(DEFAULT_SOUND_ENGINE_ENABLED);
        getInstance().durationModifierFactor.set(DEFAULT_DURATION_MODIFIER_FACTOR);
        getInstance().soundDistribution.set(DEFAULT_SOUND_DISTRIBUTION);
        getInstance().soundLimiterEnabled.set(DEFAULT_SOUND_LIMITER_ENABLED);
    }

    public static boolean isSoundEngineEnabled() {
        return getInstance().soundEngineEnabled.get();
    }

    public static void setSoundEngineEnabled(boolean enabled) {
        getInstance().soundEngineEnabled.set(enabled);
    }

    public static double getDurationModifierFactor() {
        return getInstance().durationModifierFactor.get();
    }

    public static void setDurationModifierFactor(double factor) {
        getInstance().durationModifierFactor.set(factor);
    }

    public static SoundDistribution getSoundDistribution() {
        return getInstance().soundDistribution.get();
    }

    public static void setSoundDistribution(SoundDistribution distribution) {
        getInstance().soundDistribution.set(distribution);
        SoundEngine.setAmbientSoundDistribution(distribution);
    }

    public static boolean isSoundLimiterEnabled() {
        return getInstance().soundLimiterEnabled.get();
    }

    public static void setSoundLimiterEnabled(boolean enabled) {
        getInstance().soundLimiterEnabled.set(enabled);
        SoundLimiterEngine.setEnabled(enabled);
    }
}
