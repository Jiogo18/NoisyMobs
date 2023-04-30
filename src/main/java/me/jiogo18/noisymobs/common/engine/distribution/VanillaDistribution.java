package me.jiogo18.noisymobs.common.engine.distribution;

import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.config.distribution.VanillaConfig;

public class VanillaDistribution extends AbstractDistribution {
    private boolean useBaseTicks = true;
    private double rayleighScaleSqrParam = 986.27;

    public VanillaDistribution() {
        super();
        this.reloadConfig();
    }

    @Override
    public int getRandomTicks(double baseTicks, double durationModifier) {
        float r = random.nextFloat();
        double ticks = 0;

        if (this.useBaseTicks)
            ticks = baseTicks;

        // Vanilla distribution : Rayleigh with σ² = 986.27
        // ticks = base + sqrt(-2*986.27*ln(1-r))
        ticks += Math.sqrt(-2 * rayleighScaleSqrParam * Math.log(1 - r));
        ticks *= durationModifier;

        return (int) Math.round(ticks);
    }

    private static VanillaConfig getConfig() {
        return ConfigManager.getInstance().vanillaConfig;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.useBaseTicks = getConfig().keepMinDuration.get();
        this.rayleighScaleSqrParam = getConfig().rayleighScaleSqrParam.get();
    }
}
