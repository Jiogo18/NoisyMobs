package me.jiogo18.noisymobs.common.engine;

import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.engine.distribution.AbstractDistribution;
import me.jiogo18.noisymobs.common.engine.distribution.SoundDistribution;
import net.minecraft.entity.MobEntity;

public class SoundEngine {

    private SoundEngine() {
    }

    protected static AbstractDistribution ambientSoundDistribution = null;

    public static int getNextAmbientSoundTicks(MobEntity mob) {
        if (ambientSoundDistribution == null)
            ambientSoundDistribution = ConfigManager.getSoundDistribution().create();
        return ambientSoundDistribution.getRandomTicks(mob);
    }

    public static void setAmbientSoundDistribution(SoundDistribution distribution) {
        if (distribution != null)
            ambientSoundDistribution = distribution.create();
    }

    public static void reloadAmbientSound() {
        if (ambientSoundDistribution != null)
            ambientSoundDistribution.reloadConfig();
    }
}
