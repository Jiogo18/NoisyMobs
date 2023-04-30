package me.jiogo18.noisymobs.client.screen;

import me.jiogo18.noisymobs.NoisyMobsMod;
import me.jiogo18.noisymobs.client.screen.distribution.CustomizeScreen;
import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.engine.distribution.SoundDistribution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class ConfigScreen extends AbstractConfigScreen {
    /** Options */
    private IteratableOption soundDistributionOption;
    private IteratableOption customizeButton;
    private SliderPercentageOption durationModifierFactorOption;
    private BooleanOption soundLimiterOption;

    public ConfigScreen(Minecraft mc, Screen parentScreen) {
        super(mc, parentScreen, new TranslationTextComponent("noisymobs.gui.config.title", NoisyMobsMod.NAME));
    }

    @Override
    protected void initConfigButtons() {
        // A boolean option to enable/disable the engine
        this.addBigOption(new BooleanOption(
                "noisymobs.gui.config.engine_enabled",
                gs -> ConfigManager.isSoundEngineEnabled(),
                (gs, newValue) -> setEngineEnabled(newValue)),
                "noisymobs.gui.config.engine_enabled.tooltip");

        // An iteratable option to choose the sound distribution
        soundDistributionOption = this.addBigOption(new IteratableOption(
                "noisymobs.gui.config.sound_distribution",
                (gs, newValue) -> setNextSoundDistribution(newValue),
                (gs, option) -> {
                    String distributionName = ConfigManager.getSoundDistribution().getTranslateKey();
                    return new StringTextComponent(
                            I18n.get("noisymobs.gui.config.sound_distribution") + ": " + I18n.get(distributionName));
                }),
                "noisymobs.gui.config.sound_distribution.tooltip");

        // A button to customize the sound distribution
        customizeButton = this.addBigOption(new IteratableOption(
                "noisymobs.gui.config.customize",
                (gs, newValue) -> this.getMinecraft().setScreen(CustomizeScreen.create(this.minecraft, this)),
                (gs, option) -> new StringTextComponent(I18n.get("noisymobs.gui.config.customize"))),
                "noisymobs.gui.config.customize.tooltip");

        // A slider to choose the duration factor
        durationModifierFactorOption = this.addBigOption(new SliderPercentageOption(
                "noisymobs.gui.config.duration_modifier_factor",
                0.0D, 1.0D, 0.01F,
                gs -> ConfigManager.getDurationModifierFactor(),
                (gs, newValue) -> ConfigManager.setDurationModifierFactor(Math.round(newValue * 100) / 100.0D),
                (gs, option) -> new StringTextComponent(
                        I18n.get("noisymobs.gui.config.duration_modifier_factor") + ": " + option.get(gs))),
                "noisymobs.gui.config.duration_modifier_factor.tooltip");

        // A boolean option to enable/disable the sound limiter
        soundLimiterOption = this.addBigOption(new BooleanOption(
                "noisymobs.gui.config.sound_limiter",
                gs -> ConfigManager.isSoundLimiterEnabled(),
                (gs, newValue) -> ConfigManager.setSoundLimiterEnabled(newValue)),
                "noisymobs.gui.config.sound_limiter.tooltip");

        this.updateOptions();
        this.bottomButtons = BottomButtons.RESET_DONE;
    }

    private void updateOptions() {
        boolean engineEnabled = ConfigManager.isSoundEngineEnabled();
        this.findOption(soundDistributionOption).ifPresent(b -> b.active = engineEnabled);
        this.findOption(customizeButton).ifPresent(b -> b.active = engineEnabled);
        this.findOption(durationModifierFactorOption).ifPresent(b -> b.active = engineEnabled);
        this.findOption(soundLimiterOption).ifPresent(b -> b.active = engineEnabled);
    }

    private void setEngineEnabled(boolean newValue) {
        ConfigManager.setSoundEngineEnabled(newValue);
        this.updateOptions();
    }

    private void setNextSoundDistribution(int newValue) {
        SoundDistribution[] values = SoundDistribution.values();
        SoundDistribution current = ConfigManager.getSoundDistribution();
        SoundDistribution next = values[(current.ordinal() + newValue) % values.length];
        ConfigManager.setSoundDistribution(next);
    }

    @Override
    public void reset() {
        ConfigManager.reset();
        reload();
    }
}
