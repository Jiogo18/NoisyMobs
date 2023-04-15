package com.jiogo18.noisymobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ChickenEntity.class)
public abstract class MixinChickenEntity extends AnimalEntity {
	protected MixinChickenEntity(EntityType<? extends AnimalEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
	protected void playStepSound(BlockPos pos, BlockState blockState, CallbackInfo info) {
		ChickenEntity chicken = (ChickenEntity) (Object) this;
		float volume = 0.15F + this.random.nextFloat() * 0.1F;
		float pitch = 0.9F + this.random.nextFloat() * 0.15F;
		chicken.playSound(SoundEvents.CHICKEN_STEP, volume, pitch);
	}
}
