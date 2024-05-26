package dev.buildtool.satako.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (visible) {
            this.hovered = selected;
            int k = this.getTextureY();
            context.drawTexture(WIDGETS_LOCATION, this.getX(), this.getY(), 0, 46 + k * 20, this.width, this.height);
            this.renderButton(context, mouseX, mouseY, delta);
        }
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isSelected()) {
            i = 2;
        }

        return 46 + i * 20;
    }
}
