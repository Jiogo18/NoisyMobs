package com.jiogo18.noisymobs.engine;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.util.SoundEvent;

public class SoundPlayer {
	private SoundPlayer() {
	}

	public static boolean isInteractingWithPlayer(MobEntity mob) {
		if (mob instanceof AbstractVillagerEntity) {
			AbstractVillagerEntity villager = (AbstractVillagerEntity) mob;
			return villager.isTrading();
		}
		return false;
	}

	public static void playAmbientSound(MobEntity mob, SoundEvent sound, float volume, float pitch) {
		if (isInteractingWithPlayer(mob) ||
				SoundLimiterEngine.getAmbientSoundLimiter().canPlayNewSound(mob.position())) {
			mob.playSound(sound, volume, pitch);
		}
	}
}
