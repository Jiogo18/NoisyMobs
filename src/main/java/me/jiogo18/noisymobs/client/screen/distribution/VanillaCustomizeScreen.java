package me.jiogo18.noisymobs.client.screen.distribution;

import me.jiogo18.noisymobs.NoisyMobsMod;
import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.config.distribution.VanillaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderMultiplierOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class VanillaCustomizeScreen extends CustomizeScreen {
    public VanillaCustomizeScreen(Minecraft mc, Screen parentScreen) {
        super(mc, parentScreen, new TranslationTextComponent("noisymobs.gui.config.vanilla.title", NoisyMobsMod.NAME));
    }

    private VanillaConfig getConfig() {
        return ConfigManager.getInstance().vanillaConfig;
    }

    @Override
    protected void initConfigButtons() {
        super.initConfigButtons();

        this.addBigOption(new BooleanOption(
                "noisymobs.gui.config.vanilla.keep_min_duration",
                gameSettings -> getConfig().keepMinDuration.get(),
                (gameSettings, value) -> getConfig().keepMinDuration.set(value)),
                "noisymobs.gui.config.vanilla.keep_min_duration.tooltip");

        this.addBigOption(new SliderMultiplierOption(
                "noisymobs.gui.config.vanilla.rayleigh_scale_sqr_param",
                1.0D, 10000000.0D, 0.01F,
                gameSettings -> getConfig().rayleighScaleSqrParam.get(),
                (gameSettings, value) -> getConfig().rayleighScaleSqrParam.set(Math.round(value * 100) / 100.0D),
                (gameSettings, option) -> new StringTextComponent(
                        I18n.get("noisymobs.gui.config.vanilla.rayleigh_scale_sqr_param") + ": "
                                + option.get(gameSettings))),
                "noisymobs.gui.config.vanilla.rayleigh_scale_sqr_param.tooltip");

    }

    @Override
    public void reset() {
        getConfig().reset();
        reload();
    }
}
