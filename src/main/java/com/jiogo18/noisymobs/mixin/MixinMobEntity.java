package com.jiogo18.noisymobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jiogo18.noisymobs.engine.SoundLimiterEngine;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity {
	protected MixinMobEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	private void resetAmbientSoundTime() {
		MobEntity mob = (MobEntity) (Object) this;

		float r1 = this.random.nextFloat();
		float r2 = this.random.nextFloat();

		// Longer minimun interval between sounds
		mob.ambientSoundTime = (int) (-mob.getAmbientSoundInterval() * r1 * r2 * 2);
	}

	@Shadow
	protected abstract SoundEvent getAmbientSound();

	@Override
	public void baseTick() {
		super.baseTick();
		MobEntity mob = (MobEntity) (Object) this;
		mob.level.getProfiler().push("mobBaseTick");
		if (mob.isAlive()) {
			mob.ambientSoundTime++;
			if (mob.ambientSoundTime >= 0) {
				if (this.random.nextInt(200) < 1) { // ~ every 10 seconds
					this.resetAmbientSoundTime();
					mob.playAmbientSound();
				}
			}
		}
		mob.level.getProfiler().pop();
	}

	public boolean isInteractingWithPlayer() {
		MobEntity mob = (MobEntity) (Object) this;
		if (mob instanceof AbstractVillagerEntity) {
			AbstractVillagerEntity villager = (AbstractVillagerEntity) mob;
			return villager.isTrading();
		}
		return false;
	}

	@Inject(method = "playAmbientSound", at = @At("HEAD"), cancellable = true)
	public void playAmbientSound(CallbackInfo info) {
		info.cancel();
		SoundEvent soundevent = this.getAmbientSound();
		if (soundevent != null) {
			if (isInteractingWithPlayer() ||
					SoundLimiterEngine.getAmbientSoundLimiter().canPlayNewSound(this.position())) {
				// Can speak quieter than normal
				float volume = Math.max(0, this.getSoundVolume() - this.random.nextFloat());
				this.playSound(soundevent, volume, this.getVoicePitch());
			}
		}
	}
}
