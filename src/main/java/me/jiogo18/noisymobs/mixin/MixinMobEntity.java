package me.jiogo18.noisymobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.engine.SoundEngine;
import me.jiogo18.noisymobs.common.engine.SoundPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity {
    protected MixinMobEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Shadow
    protected abstract SoundEvent getAmbientSound();

    @Shadow
    public int ambientSoundTime;

    @Shadow
    public abstract void playAmbientSound();

    private void resetCustomAmbientSoundTime() {
        this.ambientSoundTime = -SoundEngine.getNextAmbientSoundTicks((MobEntity) (Object) this);
    }

    @Inject(method = "resetAmbientSoundTime", at = @At("HEAD"), cancellable = true)
    protected void resetAmbientSoundTime(CallbackInfo info) {
        if (!ConfigManager.isSoundEngineEnabled())
            return;
        info.cancel();
        resetCustomAmbientSoundTime();
    }

    @Inject(method = "baseTick", at = @At("HEAD"), cancellable = true)
    public void baseTick(CallbackInfo info) {
        if (!ConfigManager.isSoundEngineEnabled())
            return;
        super.baseTick();
        this.level.getProfiler().push("mobBaseTick");
        if (this.isAlive()) {
            this.ambientSoundTime++;
            if (this.ambientSoundTime >= 0) {
                resetCustomAmbientSoundTime();
                this.playAmbientSound();
            }
        }
        this.level.getProfiler().pop();
        info.cancel();
    }

    @Inject(method = "playAmbientSound", at = @At("HEAD"), cancellable = true)
    public void playAmbientSound(CallbackInfo info) {
        if (!ConfigManager.isSoundEngineEnabled())
            return;
        info.cancel();
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            // Can speak quieter than normal
            float volume = Math.max(0, this.getSoundVolume() - this.random.nextFloat());
            SoundPlayer.playAmbientSound((MobEntity) (Object) this, soundevent, volume, this.getVoicePitch());
        }
    }
}
