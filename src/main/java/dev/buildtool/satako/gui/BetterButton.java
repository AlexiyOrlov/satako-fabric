package dev.buildtool.satako.gui;

import dev.buildtool.satako.Constants;
import dev.buildtool.satako.api.Scrollable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BetterButton extends ButtonWidget implements Scrollable {
    protected boolean verticallyScrollable, horizontallyScrollable;
    protected int verticalScrollAmount = height, horizontalScrollAmount = width;

    public BetterButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    public BetterButton(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
    }

    public BetterButton(int x, int y, Text text, PressAction action, TooltipSupplier tooltipSupplier) {
        this(x, y, MinecraftClient.getInstance().textRenderer.getWidth(text.getString()) + 8, Constants.BUTTON_HEIGHT, text, action, tooltipSupplier);
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
