package dev.buildtool.satako.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.buildtool.satako.Constants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.List;

public class InventoryScreen<S extends ScreenHandler> extends HandledScreen<S> {
    protected boolean drawBorders;
    protected int centerX, centerY, leftPosition, topPosition;

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
        leftPosition = (width - backgroundWidth) / 2;
        topPosition = (height - backgroundHeight) / 2;
    }

    @Override
    protected void drawBackground(DrawContext matrices, float delta, int mouseX, int mouseY) {
        List<Slot> slots = getSlots();
        GlStateManager._clearColor(1, 1, 1, 1);
        for (Slot slot : slots) {
            if (slot.isEnabled()) {
                int sx = slot.x;
                int sy = slot.y;
                if (slot instanceof BetterSlot betterSlot) {
                    if (betterSlot.getTexture() == null) {
                        matrices.fill(sx + leftPosition, sy + topPosition, sx + leftPosition + 16, sy + topPosition + 16, betterSlot.getColor().getIntColor());
                    } else {
                        matrices.fill(sx + leftPosition, sy + topPosition, sx + leftPosition + 16, sy + topPosition + 16, 0xff666666);
                    }
                } else {
                    matrices.fill(sx + leftPosition, sy + topPosition, sx + leftPosition + 16, sy + topPosition + 16, 0xff666666);
                }
            }
        }
//        RenderSystem.setShaderColor(1, 1, 1, 1);
//        for (Slot slot : getSlots()) {
//            if (slot.isEnabled()) {
//                int slotX = slot.x;
//                int slotY = slot.y;
//                TextureManager textureManager = this.client.getTextureManager();
//                if (slot instanceof BetterSlot betterSlot) {
//                    if (betterSlot.hasTexture()) {
//                        textureManager.bindTexture(betterSlot.texture);
//                        matrices.draw(matrices, slotX + this.x, slotY + y, 0, 0, 16, 16);
//                    } else {
//                        fill(matrices, slotX + x, slotY + y, slotX + x + 16, slotY + y + 16, betterSlot.color.getIntColor());
//                    }
//                } else {
//                    //draw grey background by default for vanilla slots
//                    Pair<Identifier, Identifier> atlasAndSprite = slot.getBackgroundSprite();
//                    if (atlasAndSprite != null) {
//                        Identifier background = atlasAndSprite.getSecond();
//                        if (background.getNamespace().equals("minecraft"))
//                            textureManager.bindTexture(Constants.GREY_TEXTURE);
//                        else
//                            textureManager.bindTexture(background);
//                    } else {
//                        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
//                        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//                        RenderSystem.setShaderTexture(0, Constants.GREY_TEXTURE);
//                        drawTexture(matrices, slotX + x, slotY + y, 0, 0, 16, 16);
//                    }
//
//                }
//            }
//        }

        if (drawBorders) {
            int blueColor = Constants.BLUE.getIntColor();
            matrices.drawHorizontalLine(x, x + backgroundWidth, y, blueColor);
            matrices.drawHorizontalLine(x, x + backgroundWidth, y + backgroundHeight, blueColor);
            matrices.drawVerticalLine(x, y, y + backgroundHeight, blueColor);
            matrices.drawVerticalLine(x + backgroundWidth, y, y + backgroundHeight, blueColor);
        }
    }


    @Override
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    protected List<Slot> getSlots() {
        return handler.slots;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Element child : children()) {
            child.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
