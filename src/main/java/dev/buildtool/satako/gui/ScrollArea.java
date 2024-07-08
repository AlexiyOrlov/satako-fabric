package dev.buildtool.satako.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.buildtool.satako.IntegerColor;
import dev.buildtool.satako.api.Hideable;
import dev.buildtool.satako.api.Positionable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.sound.SoundManager;
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
            this.setY(y + 20);
        buttonLeft = x + width - 20;
        if (message.getString().isEmpty())
            bottomButtonTop = this.getY() + height / 2;
        else bottomButtonTop = this.getY() + height / 2 - 20;
        this.color = color;
        this.elements = elements;
        for (Object object : this.elements) {
            if (object instanceof Positionable positionable) {
                positionable.setYPosition(this.getY() + positionable.getYPosition());
                positionable.setXPosition(this.getX() + positionable.getXPosition());
                if (object instanceof Hideable hideable) {
                    hideable.setHidden(positionable.getYPosition() < this.getY() || positionable.getYPosition() + positionable.getElementHeight() > this.getY() + height);
                }
                if (positionable.getYPosition() > highest)
                    highest = positionable.getYPosition();
            } else if (object instanceof ClickableWidget clickableWidget) {
                clickableWidget.setX(x + clickableWidget.getX());
                clickableWidget.setY(y + clickableWidget.getY());
                clickableWidget.visible = clickableWidget.getY() >= y && clickableWidget.getY() + clickableWidget.getHeight() <= this.getY() + getHeight();
                if (clickableWidget.getY() > highest)
                    highest = clickableWidget.getY();
            }
        }
        for (Object element : elements) {
            if (element instanceof Positionable positionable) {
                if (positionable.getYPosition() == highest)
                    bottomElement = element;
            } else if (element instanceof ClickableWidget clickableWidget) {
                if (clickableWidget.getY() == highest)
                    bottomElement = clickableWidget;
            }
        }
        maxScrollDistance = bottomElement instanceof Positionable positionable ? positionable.getYPosition() + positionable.getElementHeight() : bottomElement instanceof ClickableWidget clickableWidget ? clickableWidget.getY() + clickableWidget.getHeight() : 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX > buttonLeft && mouseX < buttonLeft + 20 && mouseY > getY() && mouseY < (getY() + getHeight()) / 2f)
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
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color.getIntColor());
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (!getMessage().getString().isEmpty())
            context.drawCenteredTextWithShadow(textRenderer, getMessage(), getX() + width / 2, getY() - 15, 0xffffff);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//        RenderSystem.disableTexture();
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuffer();
        int offsetY = getMessage().getString().isEmpty() ? 0 : 10;
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(getX() + width, getY(), 0).color(color.getRed(), color.getGreen(), 128, 255).next();
        bufferBuilder.vertex(buttonLeft, getY(), 0).color(color.getRed(), color.getGreen(), 128, 255).next();
        bufferBuilder.vertex(buttonLeft, bottomButtonTop + offsetY, 0).color(color.getRed(), color.getGreen(), 128, 255).next();
        bufferBuilder.vertex(getX() + width, bottomButtonTop + offsetY, 0).color(color.getRed(), color.getGreen(), 128, 255).next();

        bufferBuilder.vertex(buttonLeft + 20, bottomButtonTop + offsetY, 0).color(color.getRed(), 129, color.getBlue(), 255).next();
        bufferBuilder.vertex(buttonLeft, bottomButtonTop + offsetY, 0).color(color.getRed(), 128, color.getBlue(), 255).next();
        bufferBuilder.vertex(buttonLeft, bottomButtonTop + height / 2f, 0).color(color.getRed(), 128, color.getBlue(), 255).next();
        bufferBuilder.vertex(buttonLeft + 20, bottomButtonTop + height / 2f, 0).color(color.getRed(), 128, color.getBlue(), 255).next();
        tesselator.draw();
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("+"), buttonLeft + 10, getY() + height / 4, 0xffffff);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("-"), buttonLeft + 10, (bottomButtonTop + height / 4) - 10, 0xffffff);
        if (scrollDirection != 0) {
            if (scrolled == 0 || (scrolled > -(maxScrollDistance - height) || scrolled < -(maxScrollDistance - height) && scrollDirection == 1) && (scrolled <= 0 || scrollDirection == -1)) {
                for (Object guiEventListener : elements) {
                    if (guiEventListener instanceof Positionable positionable3) {
                        positionable3.setYPosition(positionable3.getYPosition() + scrollDirection * 20);
                        if (positionable3 instanceof Hideable hideable) {
                            hideable.setHidden(positionable3.getYPosition() < getY() || positionable3.getYPosition() + positionable3.getElementHeight() > getY() + height);
                        }
                    } else if (guiEventListener instanceof ClickableWidget a) {
                        a.setY(a.getY() + scrollDirection * 20);
                        a.visible = a.getY() > getY() && a.getY() + a.getHeight() < getY() + height;
                    }
                }
                scrolled += scrollDirection * 20;
            }
        }
    }

    @Override
    public void playDownSound(SoundManager soundManager) {

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
