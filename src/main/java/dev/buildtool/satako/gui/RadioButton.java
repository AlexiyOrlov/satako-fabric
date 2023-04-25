package dev.buildtool.satako.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class RadioButton extends BetterButton {
    public boolean selected;
    private final List<RadioButton> neighbours = new ArrayList<>();

    public RadioButton(int x, int y, Text message) {
        this(x, y, message, button -> {
        });
    }

    public RadioButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    public RadioButton(int x, int y, Text text, PressAction pressAction) {
        this(x, y, MinecraftClient.getInstance().textRenderer.getWidth(text) + 8, 20, text, pressAction);
    }

    public void addNeighbour(RadioButton radioButton) {
        neighbours.add(radioButton);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, button);
        if (clicked) {
            selected = true;
            neighbours.forEach(radioButton -> radioButton.selected = false);
        }
        return clicked;
    }

    public RadioButton(int x, int y, Text text, PressAction action, TooltipSupplier tooltipSupplier) {
        super(x, y, text, action, tooltipSupplier);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.hovered = selected;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
        int j = this.isHovered() ? 0xFFFFFF : 0xA0A0A0;
        ClickableWidget.drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }
}
