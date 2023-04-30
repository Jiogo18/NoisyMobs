package me.jiogo18.noisymobs.common.engine.distribution;

import java.util.Random;

import me.jiogo18.noisymobs.common.config.ConfigManager;
import net.minecraft.entity.MobEntity;

public abstract class AbstractDistribution {
    protected static final Random random = new Random();

    private static double durationModifierFactor = 0.2;

    protected AbstractDistribution() {
    }

    public int countEntitiesArround(MobEntity mob) {
        return mob.level
                .getEntitiesOfClass(mob.getClass(), mob.getBoundingBox().inflate(8.0D, 4.0D, 8.0D)).size();
    }

    public double getDurationModifier(MobEntity mob) {
        if (durationModifierFactor == 0)
            return 1;
        double entitiesAround = this.countEntitiesArround(mob);
        return Math.min(Math.max(1.0, Math.pow(2, entitiesAround * durationModifierFactor)), 10000);
    }

    public abstract int getRandomTicks(double minDuration, double durationModifier);

    public int getRandomTicks(MobEntity mob) {
        int minDuration = mob.getAmbientSoundInterval();
        double durationModifier = this.getDurationModifier(mob);
        return this.getRandomTicks(minDuration, durationModifier);
    }

    public void reloadConfig() {
        durationModifierFactor = ConfigManager.getDurationModifierFactor();
    }
}
