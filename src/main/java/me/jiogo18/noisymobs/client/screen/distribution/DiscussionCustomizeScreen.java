package me.jiogo18.noisymobs.client.screen.distribution;

import me.jiogo18.noisymobs.NoisyMobsMod;
import me.jiogo18.noisymobs.common.config.ConfigManager;
import me.jiogo18.noisymobs.common.config.distribution.DiscussionConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderMultiplierOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DiscussionCustomizeScreen extends CustomizeScreen {

    public DiscussionCustomizeScreen(Minecraft mc, Screen parentScreen) {
        super(mc, parentScreen,
                new TranslationTextComponent("noisymobs.gui.config.discussion.title",
                        NoisyMobsMod.NAME));
    }

    private DiscussionConfig getConfig() {
        return ConfigManager.getInstance().discussionConfig;
    }

    @Override
    protected void initConfigButtons() {
        super.initConfigButtons();
        this.addBigOption(new BooleanOption(
                "noisymobs.gui.config.discussion.use_min_duration",
                gs -> getConfig().useMinDuration.get(),
                (gs, newValue) -> getConfig().useMinDuration.set(newValue)),
                "noisymobs.gui.config.discussion.use_min_duration.tooltip");
        this.addBigOption(new SliderPercentageOption(
                "noisymobs.gui.config.discussion.probability_to_keep_discussion",
                0.0D, 1.0D, 0.001F,
                gs -> getConfig().probabilityToKeepDiscussion.get(),
                (gs, newValue) -> getConfig().probabilityToKeepDiscussion
                        .set(Math.round(newValue * 1000) / 1000.0D),
                (gs, option) -> new StringTextComponent(
                        I18n.get("noisymobs.gui.config.discussion.probability_to_keep_discussion")
                                + ": "
                                + getConfig().probabilityToKeepDiscussion.get())),
                "noisymobs.gui.config.discussion.probability_to_keep_discussion.tooltip");
        this.addBigOption(new SliderMultiplierOption(
                "noisymobs.gui.config.discussion.maximum_duration",
                100.0D, 1000000.0D, 1.0F,
                gs -> (double) getConfig().maximumDuration.get(),
                (gs, newValue) -> getConfig().maximumDuration.set((int) Math.round(newValue)),
                (gs, option) -> new StringTextComponent(
                        I18n.get("noisymobs.gui.config.discussion.maximum_duration")
                                + ": "
                                + getConfig().maximumDuration.get())),
                "noisymobs.gui.config.discussion.maximum_duration.tooltip");
    }

    @Override
    public void reset() {
        getConfig().reset();
        reload();
    }
}
