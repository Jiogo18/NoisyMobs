package me.jiogo18.noisymobs.client.screen;

import java.util.Optional;

import com.mojang.blaze3d.matrix.MatrixStack;

import me.jiogo18.noisymobs.NoisyMobsMod;
import me.jiogo18.noisymobs.common.config.ConfigManager;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class AbstractConfigScreen extends Screen {
    /** Distance from top of the screen to this GUI's title */
    private static final int TITLE_HEIGHT = 8;

    /** Distance from top of the screen to the options row list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    /** Distance from bottom of the screen to the options row list's bottom */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    /** Height of each item in the options row list */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BOTTOM_BUTTON_WIDTH = 200;
    /** Widget of the "Reset" button */
    private static final int BOTTOM_2BUTTONS_WIDTH = 150;
    /** Height of a button */
    private static final int BOTTOM_BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int BOTTOM_BUTTON_TOP_OFFSET = 26;
    /** Separation between the "Reset" and "Done" buttons */
    private static final int BUTTONS_SEPARATION = 10;

    /** List of options rows shown on the screen */
    protected OptionsRowList optionsRowList;

    /** The parent screen of this screen */
    protected final Screen parentScreen;

    /** Buttons to add to the bottom of the screen */
    protected BottomButtons bottomButtons = BottomButtons.DONE;

    protected enum BottomButtons {
        DONE, RESET_DONE
    }

    protected AbstractConfigScreen(Minecraft mc, Screen parentScreen, ITextComponent title) {
        super(title);
        this.minecraft = mc;
        this.parentScreen = parentScreen;
    }

    protected abstract void initConfigButtons();

    public void reset() {
        NoisyMobsMod.LOGGER.error("This screen has no reset function");
    }

    @Override
    public void init() {
        // Create the options row list
        this.optionsRowList = new OptionsRowList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT);

        initConfigButtons();

        this.children.add(this.optionsRowList);

        // Add the bottom buttons (Done and Reset)
        if (this.bottomButtons == BottomButtons.RESET_DONE) {
            // Add the "Reset" button
            this.addButton(new Button(
                    (this.width - BUTTONS_SEPARATION) / 2 - BOTTOM_2BUTTONS_WIDTH,
                    this.height - BOTTOM_BUTTON_TOP_OFFSET,
                    BOTTOM_2BUTTONS_WIDTH, BOTTOM_BUTTON_HEIGHT,
                    new TranslationTextComponent("noisymobs.gui.reset"),
                    button -> this.reset()));
            // Add the "Done" button
            this.addButton(new Button(
                    (this.width + BUTTONS_SEPARATION) / 2,
                    this.height - BOTTOM_BUTTON_TOP_OFFSET,
                    BOTTOM_2BUTTONS_WIDTH, BOTTOM_BUTTON_HEIGHT,
                    new TranslationTextComponent("gui.done"),
                    button -> this.onClose()));
        } else {
            // Add the "Done" button
            this.addButton(new Button(
                    (this.width - BOTTOM_BUTTON_WIDTH) / 2,
                    this.height - BOTTOM_BUTTON_TOP_OFFSET,
                    BOTTOM_BUTTON_WIDTH, BOTTOM_BUTTON_HEIGHT,
                    new TranslationTextComponent("gui.done"),
                    button -> this.onClose()));
        }
    }

    protected <T extends AbstractOption> T addBigOption(T button, String tooltipKey) {
        this.optionsRowList.addBig(button);
        Minecraft mc = getMinecraft();
        button.setTooltip(mc.font.split(new TranslationTextComponent(tooltipKey), 200));
        return button;
    }

    protected <T extends AbstractOption> Optional<Widget> findOption(T option) {
        return Optional.ofNullable(this.optionsRowList.findOption(option));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        // Options row list must be rendered here, otherwise the GUI will be broken
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        this.getMinecraft().setScreen(parentScreen);
    }

    public void reload() {
        this.children.clear();
        this.init();
    }
}
