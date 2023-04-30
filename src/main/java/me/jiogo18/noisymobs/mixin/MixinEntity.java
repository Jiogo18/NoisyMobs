package me.jiogo18.noisymobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.engine.SoundLimiterEngine;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    public abstract Vector3d position();

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    protected void playStepSound(BlockPos pos, BlockState blockState, CallbackInfo info) {
        if (!ConfigManager.isSoundEngineEnabled())
            return;
        if (!SoundLimiterEngine.getStepSoundLimiter().canPlayNewSound(this.position()))
            info.cancel();
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;playStepSound(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"), cancellable = true)
    protected void move(MoverType type, Vector3d direction, CallbackInfo info) {
        if (!ConfigManager.isSoundEngineEnabled())
            return;
        Entity entity = (Entity) (Object) this;
        if (entity instanceof AnimalEntity) {
            AnimalEntity animal = (AnimalEntity) entity;
            if (animal.isLeashed())
                return; // Leashed animals make more noise
        } else if (entity instanceof IronGolemEntity) {
            return; // Iron golems make more noise
        }

        if (!SoundLimiterEngine.getStepSoundLimiter().canPlayNewSound(this.position()))
            info.cancel();
    }
}
