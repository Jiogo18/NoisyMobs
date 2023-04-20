package me.jiogo18.noisymobs.common.engine;

import java.util.Random;

import me.jiogo18.noisymobs.NoisyMobsMod;
import net.minecraft.entity.MobEntity;

public class SoundDistribution {
    private static final Random random = new Random();

    private SoundDistribution() {
    }

    public enum Distribution {
        VANILLA, // Vanilla distribution : Rayleigh with σ² = 986.27
        DISCUSSION, // Lots of sounds in a row, then silence for a longer time
    }

    public static int getNextAmbientSoundTicks(MobEntity mob, Distribution distribution) {
        int base = mob.getAmbientSoundInterval();
        float r = random.nextFloat();
        double ticks = 0;

        int entitiesAround = mob.level
                .getEntitiesOfClass(mob.getClass(), mob.getBoundingBox().inflate(16.0D, 4.0D, 16.0D)).size();
        double factor = Math.min(Math.max(1.0f, Math.pow(2, entitiesAround * 0.2f)), 10000);
        NoisyMobsMod.LOGGER.info("Entities around: " + entitiesAround + " factor: " + factor);

        switch (distribution) {
            case VANILLA:
                // Vanilla distribution : Rayleigh with σ² = 986.27
                // ticks = base + sqrt(-2*986.27*ln(1-r))
                ticks = base + Math.sqrt(-2 * 986.27 * Math.log(1 - r));
                ticks *= factor;
                break;

            case DISCUSSION:
                if (r < 0.5) {
                    // uniform distribution between 10 and 40 ticks
                    ticks = random.nextInt(10, 40);
                } else {
                    // uniform distribution between 100 and 1000 ticks
                    ticks = random.nextInt(100, 1000) * factor;
                }
                break;

            default:
                return 0;
        }

        return (int) Math.round(ticks);
    }

    public static int getNextAmbientSoundTicks(MobEntity mob) {
        return getNextAmbientSoundTicks(mob, Distribution.DISCUSSION);
    }
}
