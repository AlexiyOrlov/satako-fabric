package dev.buildtool.satako.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import dev.buildtool.satako.Constants;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class InventoryScreen<S extends ScreenHandler> extends AbstractInventoryScreen<S> {
    protected boolean drawBorders;
    protected int centerX, centerY;

    public InventoryScreen(S screenHandler, PlayerInventory playerInventory, Text text, boolean drawBorders) {
        super(screenHandler, playerInventory, text);
        this.drawBorders = drawBorders;
    }

    @Override
    protected void init() {
        int maxX = 0, minX = x, maxY = 0;
        for (Slot slot : getSlots()) {
            int x = slot.x;
            if (x > maxX)
                maxX = x;
            if (minX > x)
                minX = x;
            int y = slot.y;
            if (y > maxY)
                maxY = y;
        }
        backgroundWidth = maxX + Constants.SLOT_WITH_BORDER_SIZE;
        backgroundHeight = maxY + Constants.SLOT_WITH_BORDER_SIZE;
        super.init();
        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        for (Slot slot : getSlots()) {
            if (slot.isEnabled()) {
                int slotX = slot.x;
                int slotY = slot.y;
                TextureManager textureManager = this.client.getTextureManager();
                if (slot instanceof BetterSlot betterSlot) {
                    if (betterSlot.hasTexture()) {
                        textureManager.bindTexture(betterSlot.texture);
                        drawTexture(matrices, slotX + this.x, slotY + y, 0, 0, 16, 16);
                    } else {
                        fill(matrices, slotX + x, slotY + y, slotX + x + 16, slotY + y + 16, betterSlot.color.getIntColor());
                    }
                } else {
                    //draw grey background by default for vanilla slots
                    Pair<Identifier, Identifier> atlasAndSprite = slot.getBackgroundSprite();
                    if (atlasAndSprite != null) {
                        Identifier background = atlasAndSprite.getSecond();
                        if (background.getNamespace().equals("minecraft"))
                            textureManager.bindTexture(Constants.GREY_TEXTURE);
                        else
                            textureManager.bindTexture(background);
                    } else {
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                        RenderSystem.setShaderTexture(0, Constants.GREY_TEXTURE);
                        drawTexture(matrices, slotX + x, slotY + y, 0, 0, 16, 16);
                    }

                }
            }
        }

        if (drawBorders) {
            int blueColor = Constants.BLUE.getIntColor();
            drawHorizontalLine(matrices, x, x + backgroundWidth, y, blueColor);
            drawHorizontalLine(matrices, x, x + backgroundWidth, y + backgroundHeight, blueColor);
            drawVerticalLine(matrices, x, y, y + backgroundHeight, blueColor);
            drawVerticalLine(matrices, x + backgroundWidth, y, y + backgroundHeight, blueColor);
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    protected List<Slot> getSlots() {
        return handler.slots;
    }
}
