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
        int l1 = whenTrue_.getString().length();
        int l2 = whenFalse_.getString().length();
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
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        state = !state;
        if (state) {
            setMessage(whenTrue);
        } else {
            setMessage(whenFalse);
        }
    }
}
