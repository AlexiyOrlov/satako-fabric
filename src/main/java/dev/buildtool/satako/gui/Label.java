package dev.buildtool.satako.gui;

import dev.buildtool.satako.api.Scrollable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class Label extends BetterButton implements Scrollable {
    protected Screen parent;

    {
        verticalScrollAmount = 20;
    }

    public Label(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message, null);
    }

    public Label(int x, int y, int width, int height, Text message, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, button -> {
        }, tooltipSupplier);
    }

    public Label(int x, int y, Text text, PressAction action, TooltipSupplier tooltipSupplier) {
        super(x, y, text, action, tooltipSupplier);
    }

    public Label(int x, int y, Text text) {
        this(x, y, MinecraftClient.getInstance().textRenderer.getWidth(text) + 8, 18, text);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, getMessage().getString(), x, y, 0xffffff);
    }

    public Label(int x, int y, Text text, Screen parent, ButtonWidget.PressAction pressHandler) {
        super(x, y, MinecraftClient.getInstance().textRenderer.getWidth(text.getString()) + 8, 18, text, pressHandler);

        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (visible) {
            if (this.onPress != null) {
                if (parent != null)
                    parent.renderTooltip(matrices, getMessage(), getX() - 8, getY() + 18);
                else
                    drawTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, getMessage(), this.getX(), this.getY() + (this.height - 8) / 2, 16777215 | MathHelper.ceil(this.alpha * 255.0F) << 24);
            } else
                drawTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, getMessage(), this.getX(), this.getY() + (this.height - 8) / 2, 16777215 | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.onPress != null)
            return super.keyPressed(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (onPress != null)
            super.onClick(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (onPress != null)
            return super.mouseClicked(mouseX, mouseY, button);
        return false;
    }
}
