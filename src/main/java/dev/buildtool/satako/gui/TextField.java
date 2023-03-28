package dev.buildtool.satako.gui;

import dev.buildtool.satako.api.Scrollable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class TextField extends TextFieldWidget implements Scrollable {
    protected boolean verticallyScrollable, horizontallyScrollable;
    protected int verticalScrollAmount = height, horizontalScrollAmount = width;

    public TextField(int x, int y, int height, Text text) {
        super(MinecraftClient.getInstance().textRenderer, x, y, MinecraftClient.getInstance().textRenderer.getWidth(text) + 10, height, text);
    }

    public TextField(int x, int y, int height, @Nullable TextFieldWidget copyFrom, Text text) {
        super(MinecraftClient.getInstance().textRenderer, x, y, MinecraftClient.getInstance().textRenderer.getWidth(text) + 10, height, copyFrom, text);
    }

    public TextField(int X, int Y, int width) {
        super(MinecraftClient.getInstance().textRenderer, X, Y, width, 15, Text.literal(""));
    }

    @Override
    public void scroll(int direction, boolean vertically) {
        if (vertically && verticallyScrollable) {
            y += verticalScrollAmount * direction;
        }
        if (!vertically && horizontallyScrollable) {
            x += horizontalScrollAmount * direction;
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
}
