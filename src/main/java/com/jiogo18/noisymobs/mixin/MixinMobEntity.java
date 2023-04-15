package com.jiogo18.noisymobs.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity {
	private MixinMobEntity() {
		super(null, null);
	}

	private void resetAmbientSoundTime() {
		MobEntity mob = (MobEntity) (Object) this;
		mob.ambientSoundTime = -mob.getAmbientSoundInterval();
	}

	@Override
	public void baseTick() {
		super.baseTick();
		MobEntity mob = (MobEntity) (Object) this;
		mob.level.getProfiler().push("mobBaseTick");
		if (mob.isAlive()) {
			mob.ambientSoundTime++;
			if (mob.ambientSoundTime >= 0) {
				if (this.random.nextInt(2000) < 1) { // ~ every 100 seconds
					this.resetAmbientSoundTime();
					mob.playAmbientSound();
				}
			}
		}
		mob.level.getProfiler().pop();
	}
}
