package me.jiogo18.noisymobs.common.engine.distribution;

import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.config.distribution.DiscussionConfig;
import net.minecraft.entity.MobEntity;

public class DiscussionDistribution extends AbstractDistribution {
    private boolean useBaseTicks = true;
    private double probabilityToKeepDiscussion = 0.5;
    private int maximumDuration = 1000;

    public DiscussionDistribution() {
        super();
        this.reloadConfig();
    }

    @Override
    public int getRandomTicks(double baseTicks, double durationModifier) {
        float r = random.nextFloat();
        double ticks = 0;

        if (r < probabilityToKeepDiscussion) {
            // uniform distribution between 10 and 40 ticks
            ticks = random.nextInt(10, 40);
        } else {
            // uniform distribution between 100 and 1000 ticks
            ticks = random.nextInt(100, maximumDuration) * durationModifier;
        }

        if (useBaseTicks)
            ticks += baseTicks;

        return (int) Math.round(ticks);
    }

    @Override
    public int getRandomTicks(MobEntity mob) {
        float r = random.nextFloat();
        double ticks = 0;

        if (r < probabilityToKeepDiscussion) {
            // uniform distribution between 10 and 40 ticks
            ticks = random.nextInt(10, 40);
        } else {
            // uniform distribution between 100 and 1000 ticks
            double durationModifier = this.getDurationModifier(mob);
            ticks = random.nextInt(100, maximumDuration + 1) * durationModifier;
        }

        return (int) Math.round(ticks);
    }

    private static DiscussionConfig getConfig() {
        return ConfigManager.getInstance().discussionConfig;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.useBaseTicks = getConfig().useMinDuration.get();
        this.probabilityToKeepDiscussion = getConfig().probabilityToKeepDiscussion.get();
        this.maximumDuration = getConfig().maximumDuration.get();
    }
}
