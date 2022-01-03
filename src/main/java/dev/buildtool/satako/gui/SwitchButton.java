package dev.buildtool.satako.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * This button holds "true" or "false" state. Clicking the button toggles the state
 */
@SuppressWarnings("unused")
public class SwitchButton extends BetterButton {
    public boolean state;
    Text whenTrue, whenFalse;

    /**
     * @param whenTrue_  String shown when "true" is active
     * @param whenFalse_ String shown when "false" is active
     */
    public SwitchButton(int x, int y, Text whenTrue_, Text whenFalse_, boolean startState, PressAction pressable) {
        super(x, y, startState ? whenTrue_ : whenFalse_, pressable, (button, matrices, mouseX, mouseY) -> {
        });
        int l1 = whenTrue_.asString().length();
        int l2 = whenFalse_.asString().length();
        if (l1 > l2) {
            this.width = MinecraftClient.getInstance().textRenderer.getWidth(whenTrue_) + 8;
        } else {
            width = MinecraftClient.getInstance().textRenderer.getWidth(whenFalse_) + 8;
        }
        state = startState;
        if (state) {
            setMessage(whenTrue_);
        } else {
            setMessage(whenFalse_);
        }
        whenTrue = whenTrue_;
        this.whenFalse = whenFalse_;
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        boolean clicked = super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        if (clicked) {
            if (state) {
                setMessage(whenTrue);
            } else {
                setMessage(whenFalse);
            }
        }
        return clicked;
    }
}
