package me.jiogo18.noisymobs;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import me.jiogo18.noisymobs.common.engine.SoundDistribution.Distribution;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager {
    private static final ConfigManager INSTANCE;
    private static final ForgeConfigSpec SPEC;
    private static final Path CONFIG_PATH = Paths.get("config", NoisyMobsMod.MODID + ".toml");

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

    public final ForgeConfigSpec.BooleanValue soundEngineEnabled;
    public final ForgeConfigSpec.EnumValue<Distribution> soundDistribution;
    public final ForgeConfigSpec.IntValue meanSoundTime;

    public static final int DEFAULT_MEAN_SOUND_TIME = 100;

    private ConfigManager(ForgeConfigSpec.Builder builder) {
        this.soundEngineEnabled = builder.comment("Enables the sound engine")
                .translation("configured.config.client.sound_engine.enabled")
                .define("soundEngineEnabled", true);

        this.soundDistribution = builder.comment("The distribution of the sound engine")
                .translation("configured.config.client.sound_engine.distribution")
                .defineEnum("soundDistribution", Distribution.getDefault());

        builder.push("customization");
        builder.push("vanilla");
        this.meanSoundTime = builder.comment("The mean time between sounds")
                .translation("configured.config.client.sound_engine.mean_sound_time")
                .defineInRange("meanSoundTime", DEFAULT_MEAN_SOUND_TIME, 1, 12000);
        builder.pop();
        builder.pop();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public static void save() {
        SPEC.save();
    }
}
