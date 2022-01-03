package dev.buildtool.satako;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Fixed-size itemstack list
 * Created on 7/22/19.
 */
@SuppressWarnings("unused")
public class ItemList extends ArrayList<ItemStack> {
    protected int capacity;

    public ItemList(int size) {
        super(size);
        capacity = size;
        empty();
    }

    @Override
    public ItemStack set(int index, ItemStack element) {
        if (index >= super.size()) {
            super.add(index, element);
            return ItemStack.EMPTY;
        } else {
            ItemStack itemStack = get(index);
            super.set(index, element);
            return itemStack;
        }
    }

    @Override
    public ItemStack get(int index) {
        if (index >= super.size())
            return ItemStack.EMPTY;
        return super.get(index);
    }

    @Override
    public void clear() {
        empty();
    }

    protected void empty() {
        for (int i = 0; i < capacity; i++) {
            set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public int size() {
        return capacity;
    }

    @Override
    public boolean add(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            for (int i = 0; i < capacity; i++) {
                if (get(i).isEmpty()) {
                    set(i, itemStack);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack remove(int index) {
        ItemStack stack = get(index);
        set(index, ItemStack.EMPTY);
        return stack;
    }

    /**
     * @return true if the list contains only empty stacks
     */
    @Override
    public boolean isEmpty() {
        return Functions.isEmpty(this);
    }

}
