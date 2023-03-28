package dev.buildtool.satako.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.buildtool.satako.IntegerColor;
import dev.buildtool.satako.api.Hideable;
import dev.buildtool.satako.api.Positionable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class ScrollArea extends ClickableWidget {
    private final IntegerColor color;
    int buttonLeft;
    int bottomButtonTop;
    protected List<?> elements;
    private int scrollDirection;
    private int scrolled;
    private int highest;
    private final int maxScrollDistance;
    private Object bottomElement;

    public ScrollArea(int x, int y, int width, int height, Text message, IntegerColor color, List<?> elements) {
        super(x, y, width, height, message);
        if (!message.getString().isEmpty())
            this.y = y + 20;
        buttonLeft = x + width - 20;
        if (message.getString().isEmpty())
            bottomButtonTop = this.y + height / 2;
        else bottomButtonTop = this.y + height / 2 - 20;
        this.color = color;
        this.elements = elements;
        for (Object object : this.elements) {
            if (object instanceof Positionable positionable) {
                positionable.setY(this.y + positionable.getY());
                positionable.setX(this.x + positionable.getX());
                if (object instanceof Hideable hideable) {
                    hideable.setHidden(positionable.getY() < this.y || positionable.getY() + positionable.getElementHeight() > this.y + height);
                }
                if (positionable.getY() > highest)
                    highest = positionable.getY();
            } else if (object instanceof ClickableWidget clickableWidget) {
                clickableWidget.x = x + clickableWidget.x;
                clickableWidget.y = y + clickableWidget.y;
                clickableWidget.visible = clickableWidget.y >= y && clickableWidget.y + clickableWidget.getHeight() <= this.y + getHeight();
                if (clickableWidget.y > highest)
                    highest = clickableWidget.y;
            }
        }
        for (Object element : elements) {
            if (element instanceof Positionable positionable) {
                if (positionable.getY() == highest)
                    bottomElement = element;
            } else if (element instanceof ClickableWidget clickableWidget) {
                if (clickableWidget.y == highest)
                    bottomElement = clickableWidget;
            }
        }
        maxScrollDistance = bottomElement instanceof Positionable positionable ? positionable.getY() + positionable.getElementHeight() : bottomElement instanceof ClickableWidget clickableWidget ? clickableWidget.y + clickableWidget.getHeight() : 0;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX > buttonLeft && mouseX < buttonLeft + 20 && mouseY > y && mouseY < (y + getHeight()) / 2f)
            scrollDirection = -1;
        else if (mouseX > buttonLeft && mouseX < buttonLeft + 20 && mouseY > bottomButtonTop && mouseY < bottomButtonTop + getHeight() / 2f) {
            scrollDirection = 1;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    //isn't called
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        scrollDirection = 0;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        fill(matrices, x, y, x + getWidth(), y + getHeight(), color.getIntColor());
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (!getMessage().getString().isEmpty())
            drawCenteredText(matrices, textRenderer, getMessage(), x + width / 2, y - 15, 0xffffff);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuffer();
        int offsetY = getMessage().getString().isEmpty() ? 0 : 10;
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(x + width, y, 0).color(color.getRed(), color.getGreen(), 128, 255).next();
        bufferBuilder.vertex(buttonLeft, y, 0).color(color.getRed(), color.getGreen(), 128, 255).next();
        bufferBuilder.vertex(buttonLeft, bottomButtonTop + offsetY, 0).color(color.getRed(), color.getGreen(), 128, 255).next();
        bufferBuilder.vertex(x + width, bottomButtonTop + offsetY, 0).color(color.getRed(), color.getGreen(), 128, 255).next();

        bufferBuilder.vertex(buttonLeft + 20, bottomButtonTop + offsetY, 0).color(color.getRed(), 129, color.getBlue(), 255).next();
        bufferBuilder.vertex(buttonLeft, bottomButtonTop + offsetY, 0).color(color.getRed(), 128, color.getBlue(), 255).next();
        bufferBuilder.vertex(buttonLeft, bottomButtonTop + height / 2f, 0).color(color.getRed(), 128, color.getBlue(), 255).next();
        bufferBuilder.vertex(buttonLeft + 20, bottomButtonTop + height / 2f, 0).color(color.getRed(), 128, color.getBlue(), 255).next();
        tesselator.draw();
        drawCenteredText(matrices, textRenderer, Text.literal("-"), buttonLeft + 10, y + height / 4, 0xffffff);
        drawCenteredText(matrices, textRenderer, Text.literal("+"), buttonLeft + 10, (bottomButtonTop + height / 4) - 10, 0xffffff);
        if (scrollDirection != 0) {
            if (scrolled == 0 || (scrolled > -(maxScrollDistance - height) || scrolled < -(maxScrollDistance - height) && scrollDirection == 1) && (scrolled <= 0 || scrollDirection == -1)) {
                for (Object guiEventListener : elements) {
                    if (guiEventListener instanceof Positionable positionable3) {
                        positionable3.setY(positionable3.getY() + scrollDirection * 20);
                        if (positionable3 instanceof Hideable hideable) {
                            hideable.setHidden(positionable3.getY() < y || positionable3.getY() + positionable3.getElementHeight() > y + height);
                        }
                    } else if (guiEventListener instanceof ClickableWidget a) {
                        a.y = a.y + scrollDirection * 20;
                        a.visible = a.y > y && a.y + a.getHeight() < y + height;
                    }
                }
                scrolled += scrollDirection * 20;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (Object guiEventListener : elements) {
            if (guiEventListener instanceof Positionable positionable3) {
                positionable3.setY((int) (positionable3.getY() + amount * 20));
                if (positionable3 instanceof Hideable hideable) {
                    hideable.setHidden(positionable3.getY() < y || positionable3.getY() + positionable3.getElementHeight() > y + height);
                }
            } else if (guiEventListener instanceof ClickableWidget a) {
                a.y = (int) (a.y + amount * 20);
                a.visible = a.y > y && a.y + a.getHeight() < y + height;
            }
        }
        return true;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {

    }
}
