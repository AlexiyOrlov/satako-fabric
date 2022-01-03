package dev.buildtool.satako.gui;

import dev.buildtool.satako.api.Scrollable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount != 0) {
            for (Element element : this.children()) {
                if (element instanceof Scrollable scrollable) {
                    scrollable.scroll((int) Math.signum(amount), !Screen.hasAltDown());
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean isPauseScreen() {
        return client.player.getHealth() < client.player.getMaxHealth() / 2;
    }
}
