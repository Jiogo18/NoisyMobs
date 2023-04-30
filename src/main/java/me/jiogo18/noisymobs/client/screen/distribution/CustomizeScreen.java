package me.jiogo18.noisymobs.client.screen.distribution;

import me.jiogo18.noisymobs.client.screen.AbstractConfigScreen;
import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.engine.SoundEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public abstract class CustomizeScreen extends AbstractConfigScreen {

    protected CustomizeScreen(Minecraft mc, Screen parentScreen, ITextComponent title) {
        super(mc, parentScreen, title);
    }

    @Override
    protected void initConfigButtons() {
        this.bottomButtons = BottomButtons.RESET_DONE;
    }

    @Override
    public void onClose() {
        super.onClose();
        SoundEngine.reloadAmbientSound();
    }

    public static CustomizeScreen create(Minecraft mc, Screen parentScreen) {
        switch (ConfigManager.getSoundDistribution()) {
            case VANILLA:
                return new VanillaCustomizeScreen(mc, parentScreen);
            case DISCUSSION:
                return new DiscussionCustomizeScreen(mc, parentScreen);
            default:
                throw new IllegalStateException("Unexpected value: " + ConfigManager.getSoundDistribution());
        }
    }
}
