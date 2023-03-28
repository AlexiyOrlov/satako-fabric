package dev.buildtool.satako.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Label extends BetterButton {
    public Label(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message, button -> {
        });
    }

    public Label(int x, int y, int width, int height, Text message, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, button -> {
        }, tooltipSupplier);
    }

    public Label(int x, int y, Text text, PressAction action, TooltipSupplier tooltipSupplier) {
        super(x, y, text, action, tooltipSupplier);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, getMessage().getString(), x, y, 0xffffff);
    }
}
