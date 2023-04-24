package me.jiogo18.noisymobs.client.screen;

import me.jiogo18.noisymobs.ConfigManager;
import me.jiogo18.noisymobs.NoisyMobsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderMultiplierOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CustomizeScreen extends AbstractConfigScreen {

    public CustomizeScreen(Minecraft mc, Screen parentScreen) {
        // Use the super class' constructor to set the screen's title
        super(mc, parentScreen, new TranslationTextComponent("noisymobs.gui.moreConfig.title", NoisyMobsMod.NAME));
    }

    @Override
    protected void initConfigButtons() {
        // An option to change the average number ticks between sounds
        this.addBigOption(new SliderMultiplierOption(
                "noisymobs.gui.moreConfig.soundMeanTime.title",
                1, 12000,
                1.0F,
                unused -> (double) ConfigManager.getInstance().meanSoundTime.get(),
                (unused, newValue) -> ConfigManager.getInstance().meanSoundTime.set(newValue.intValue()),
                (gs, option) -> new StringTextComponent(
                        I18n.get("noisymobs.gui.moreConfig.soundMeanTime.title")
                                + ": "
                                + (int) option.get(gs))));

        this.bottomButtons = BottomButtons.RESET_DONE;
    }

    @Override
    public void reset() {
        ConfigManager.getInstance().meanSoundTime.set(ConfigManager.DEFAULT_MEAN_SOUND_TIME);
    }
}
