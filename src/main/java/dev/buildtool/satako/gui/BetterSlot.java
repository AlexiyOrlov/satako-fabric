package dev.buildtool.satako.gui;

import dev.buildtool.satako.Constants;
import dev.buildtool.satako.IntegerColor;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class BetterSlot extends Slot {
    protected IntegerColor color = Constants.BLUE;
    protected Identifier texture;
    protected boolean enabled = true;

    public BetterSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public BetterSlot setColor(IntegerColor color) {
        this.color = color;
        return this;
    }

    public BetterSlot setTexture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    public Identifier getTexture() {
        return texture;
    }

    public IntegerColor getColor() {
        return color;
    }

    /**
     * Is visible?
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
