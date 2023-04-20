package me.jiogo18.noisymobs;

import java.util.logging.Logger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod("noisymobs")
public class NoisyMobsMod {
    public static final String MODID = "noisymobs";
    public static final String NAME = "Noisy Mobs";
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private static NoisyMobsMod instance;

    public NoisyMobsMod() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
    }

    public static NoisyMobsMod getInstance() {
        return instance;
    }

    private void onLoadComplete(FMLLoadCompleteEvent event) {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            LOGGER.info("Hello from Noisy Mobs!");
        } else {
            LOGGER.info("Hello server! This mod is only for client-side");
        }
    }
}
