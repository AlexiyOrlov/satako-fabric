package dev.buildtool.satako.gui;

import dev.buildtool.satako.Constants;
import dev.buildtool.satako.api.Hideable;
import dev.buildtool.satako.api.Positionable;
import dev.buildtool.satako.api.Scrollable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BetterButton extends ButtonWidget implements Scrollable, Positionable, Hideable {
    protected boolean verticallyScrollable, horizontallyScrollable;
    protected int verticalScrollAmount = height, horizontalScrollAmount = width;
    public static final Identifier WIDGETS_LOCATION = new Identifier("textures/gui/widgets.png");


    protected TextRenderer textRenderer;

    {
        textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public BetterButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, textSupplier -> Text.literal(""));
    }

    public BetterButton(int x, int y, Text text, PressAction action) {
        this(x, y, MinecraftClient.getInstance().textRenderer.getWidth(text.getString()) + 8, Constants.BUTTON_HEIGHT, text, action);
    }

    public BetterButton(int x, int y, Text text) {
        this(x, y, MinecraftClient.getInstance().textRenderer.getWidth(text) + 8, 20, text, button -> {
        });
    }

    @Override
    public void scroll(int direction, boolean vertically) {
        if (vertically && verticallyScrollable) {
            setY(getY() + verticalScrollAmount * direction);
            ;
        }
        if (!vertically && horizontallyScrollable) {
            setX(getX() + horizontalScrollAmount * direction);
            ;
        }
    }

    @Override
    public void setScrollable(boolean vertically, boolean doScroll) {
        if (vertically)
            verticallyScrollable = doScroll;
        else
            horizontallyScrollable = doScroll;
    }

    @Override
    public void setScrollingAmount(boolean vertically, int amount) {
        if (vertically)
            verticalScrollAmount = amount;
        else
            horizontalScrollAmount = amount;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.visible = !hidden;
    }

    @Override
    public int getElementWidth() {
        return width;
    }

    @Override
    public int getElementHeight() {
        return height;
    }
}
