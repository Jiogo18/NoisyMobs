package me.jiogo18.noisymobs;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.jiogo18.noisymobs.client.screen.ConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod("noisymobs")
public class NoisyMobsMod {
    public static final String MODID = "noisymobs";
    public static final String NAME = "Noisy Mobs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    private static NoisyMobsMod instance;

    public NoisyMobsMod() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        // Register the configuration GUI factory
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen(mc, screen));
    }

    public static NoisyMobsMod getInstance() {
        return instance;
    }

    private void clientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Hello from Noisy Mobs!");
    }

    private void onLoadComplete(FMLLoadCompleteEvent event) {
        if (FMLLoader.getDist() != Dist.CLIENT) {
            LOGGER.info("Hello server! This mod is only for client-side");
        }
    }
}
