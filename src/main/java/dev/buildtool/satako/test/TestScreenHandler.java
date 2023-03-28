package dev.buildtool.satako.test;

import dev.buildtool.satako.DefaultInventory;
import dev.buildtool.satako.IntegerColor;
import dev.buildtool.satako.Satako;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerInventory;

public class TestScreenHandler extends BetterScreenHandler {
    public TestScreenHandler(int syncId, PlayerInventory inventory) {
        super(Satako.testScreenHandlerType, syncId);
        DefaultInventory defaultInventory = new DefaultInventory(1);
        addSlot(new BetterSlot(defaultInventory, 0, 0, 0).setColor(new IntegerColor(0xff4f6a7b)));
        addPlayerInventory(0, 20, inventory);
    }
}
