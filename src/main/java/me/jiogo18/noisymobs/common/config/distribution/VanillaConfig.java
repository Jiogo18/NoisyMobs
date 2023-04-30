package me.jiogo18.noisymobs.common.config.distribution;

import net.minecraftforge.common.ForgeConfigSpec;

public class VanillaConfig implements IConfig {
    public final ForgeConfigSpec.BooleanValue keepMinDuration;
    public final ForgeConfigSpec.DoubleValue rayleighScaleSqrParam;
    public static final boolean DEFAULT_KEEP_MIN_DURATION = true;
    public static final double DEFAULT_RAYLEIGH_SCALE_SQR_PARAM = 986.27;

    public VanillaConfig(ForgeConfigSpec.Builder builder) {
        builder.push("vanilla");
        this.keepMinDuration = builder
                .comment("Keep the default minimum duration (typically 80 ticks depending on entity)")
                .translation("configured.config.client.sound_engine.vanilla.keep_default_min_duration")
                .define("vanillaKeepDefaultMinDuration", DEFAULT_KEEP_MIN_DURATION);
        this.rayleighScaleSqrParam = builder
                .comment("The squared scale parameter of the Rayleigh distribution (σ²)")
                .translation("configured.config.client.sound_engine.vanilla.rayleigh_scale_sqr_param")
                .defineInRange("rayleighScaleSqrParam", DEFAULT_RAYLEIGH_SCALE_SQR_PARAM, 1, 1000000000);
        builder.pop();
    }

    @Override
    public void reset() {
        this.keepMinDuration.set(DEFAULT_KEEP_MIN_DURATION);
        this.rayleighScaleSqrParam.set(DEFAULT_RAYLEIGH_SCALE_SQR_PARAM);
    }
}
