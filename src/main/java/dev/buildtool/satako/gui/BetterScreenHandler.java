package dev.buildtool.satako.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class BetterScreenHandler extends ScreenHandler {
    public BetterScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        Slot clickedSlot = this.slots.get(index);
        ItemStack stack = ItemStack.EMPTY;
        if (clickedSlot.hasStack()) {
            ItemStack clickedStack = clickedSlot.getStack();
            stack = clickedStack.copy();
            if (clickedStack.getCount() == 0) {
                clickedSlot.setStack(ItemStack.EMPTY);
            } else {
                clickedSlot.markDirty();
            }

            if (clickedStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            clickedSlot.onTakeItem(player, clickedStack);
        }
        return stack;
    }

    protected void addPlayerInventory(int horizontalOffset, int verticalOffset, PlayerEntity player) {

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                Slot slot = new Slot(player.getInventory(), column + row * 9 + 9, column * 18 + horizontalOffset, row * 18 + verticalOffset);
                this.addSlot(slot);
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            Slot slotIn = new Slot(player.getInventory(), i1, i1 * 18 + horizontalOffset, 3 * 18 + verticalOffset);
            this.addSlot(slotIn);
        }
    }

    protected void addPlayerInventory(int horOffset, int verOffset, PlayerInventory inventory) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                Slot slot = new Slot(inventory, column + row * 9 + 9, column * 18 + horOffset, row * 18 + verOffset);
                this.addSlot(slot);
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            Slot slotIn = new Slot(inventory, i1, i1 * 18 + horOffset, 3 * 18 + verOffset);
            this.addSlot(slotIn);
        }
    }
}
