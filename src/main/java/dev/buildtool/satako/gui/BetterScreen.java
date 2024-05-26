package dev.buildtool.satako.gui;

import dev.buildtool.satako.api.Scrollable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class BetterScreen extends Screen {
    protected int centerX, centerY;

    public BetterScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount != 0) {
            for (Element element : this.children()) {
                if (element instanceof Scrollable scrollable) {
                    scrollable.scroll((int) Math.signum(verticalAmount), !Screen.hasAltDown());
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean shouldPause() {
        return client.player.getHealth() < client.player.getMaxHealth() / 2;
    }
}
