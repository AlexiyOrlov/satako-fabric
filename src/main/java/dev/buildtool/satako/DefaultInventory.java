package dev.buildtool.satako;

import dev.buildtool.satako.api.TagConvertible;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

;

/**
 * Uses {@linkplain ItemList} to store items.
 * Created on 7/20/19.
 */
public class DefaultInventory implements Inventory, TagConvertible {
    protected ItemList itemStacks;

    public DefaultInventory(int size) {
        itemStacks = new ItemList(size);
    }


    @Override
    public int size() {
        return itemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        return Functions.isEmpty(itemStacks);
    }

    @Override
    public ItemStack getStack(int var1) {
        return itemStacks.get(var1);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(itemStacks, slot, amount);
    }

    @Override
    public ItemStack removeStack(int var1) {
        return Inventories.removeStack(itemStacks, var1);
    }

    @Override
    public void setStack(int var1, ItemStack var2) {
        itemStacks.set(var1, var2);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity var1) {
        return true;
    }

    @Override
    public void clear() {
        itemStacks.clear();
    }

    /**
     * Inserts item into inventory fully or 1-by-1
     *
     * @param i stack to insert
     * @return amount inserted
     */
    public int addStack(ItemStack i) {
        int inCount = i.getCount();
        for (int j = 0; j < size(); j++) {
            ItemStack itemStack = getStack(j);
            if (Functions.areItemTypesEqual(i, itemStack)) {
                if (itemStack.isEmpty()) {
                    setStack(j, i);
                    return inCount;
                } else if (itemStack.getCount() + inCount <= itemStack.getMaxCount()) {
                    itemStack.increment(inCount);
                    i.decrement(inCount);
                    return inCount;
                } else if (itemStack.getCount() + 1 <= itemStack.getMaxCount()) {
                    itemStack.increment(1);
                    i.decrement(1);
                    return 1;
                }
            }
        }
        for (int j = 0; j < size(); j++) {
            ItemStack itemStack = getStack(j);
            if (itemStack.isEmpty()) {
                setStack(j, i);
                return inCount;
            }
        }
        return 0;
    }

    public int getEmptySlotCount() {
        return itemStacks.stream().filter(ItemStack::isEmpty).mapToInt(value -> 1).sum();
    }

    @Override
    public NbtCompound writeToTag() {
        NbtCompound nbtCompound = new NbtCompound();
        for (int i = 0; i < itemStacks.size(); i++) {
            ItemStack itemStack = itemStacks.get(i);
            nbtCompound.put("Stack#" + i, itemStack.writeNbt(new NbtCompound()));
        }
        nbtCompound.putInt("Size", itemStacks.size());
        return nbtCompound;
    }

    @Override
    public void readFromTag(NbtCompound nbtCompound) {
        int count = nbtCompound.getInt("Size");
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                ItemStack itemStack = ItemStack.fromNbt(nbtCompound.getCompound("Stack#" + i));
                setStack(i, itemStack);
            }
        }
    }
}
