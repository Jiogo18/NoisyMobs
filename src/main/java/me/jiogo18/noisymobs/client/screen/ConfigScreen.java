package me.jiogo18.noisymobs.client.screen;

import me.jiogo18.noisymobs.ConfigManager;
import me.jiogo18.noisymobs.NoisyMobsMod;
import me.jiogo18.noisymobs.common.engine.SoundDistribution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class ConfigScreen extends AbstractConfigScreen {
    /** Options */
    private IteratableOption soundDistributionOption;
    private IteratableOption customizeButton;

    public ConfigScreen(Minecraft mc, Screen parentScreen) {
        super(mc, parentScreen, new TranslationTextComponent("noisymobs.gui.config.title", NoisyMobsMod.NAME));
    }

    @Override
    protected void initConfigButtons() {
        // A boolean option to enable/disable the engine
        this.addBigOption(new BooleanOption(
                "noisymobs.gui.config.engineEnabled.title",
                gs -> ConfigManager.getInstance().soundEngineEnabled.get(),
                (gs, newValue) -> setEngineEnabled(newValue)));

        // An iteratable option to choose the sound distribution
        soundDistributionOption = this.addBigOption(new IteratableOption(
                "noisymobs.gui.config.sound_distribution.title",
                (gs, newValue) -> setNextSoundDistribution(newValue),
                (gs, option) -> getSoundDistributionText()));

        // A button to customize the sound distribution
        customizeButton = this.addBigOption(new IteratableOption(
                "noisymobs.gui.config.customize.title",
                (gs, newValue) -> this.getMinecraft().setScreen(new CustomizeScreen(this.minecraft, this)),
                (gs, option) -> new StringTextComponent(I18n.get("noisymobs.gui.config.customize.title"))));

        this.updateOptions();
        this.bottomButtons = BottomButtons.DONE;
    }

    private void updateOptions() {
        boolean engineEnabled = ConfigManager.getInstance().soundEngineEnabled.get();
        this.findOption(soundDistributionOption).ifPresent(b -> b.active = engineEnabled);
        this.findOption(customizeButton).ifPresent(b -> b.active = engineEnabled);
    }

    private void setEngineEnabled(boolean newValue) {
        ConfigManager.getInstance().soundEngineEnabled.set(newValue);
        this.updateOptions();
    }

    private StringTextComponent getSoundDistributionText() {
        String distributionName = ConfigManager.getInstance().soundDistribution.get().getTranslateKey();
        return new StringTextComponent(
                I18n.get("noisymobs.gui.config.sound_distribution.title") + ": " + I18n.get(distributionName));
    }

    private void setNextSoundDistribution(int newValue) {
        ConfigManager.getInstance().soundDistribution.set(SoundDistribution.Distribution
                .values()[(ConfigManager.getInstance().soundDistribution.get().ordinal() + newValue)
                        % SoundDistribution.Distribution.values().length]);
    }
}
