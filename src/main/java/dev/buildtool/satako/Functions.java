package dev.buildtool.satako;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class Functions {

    public static boolean isEmpty(Collection<ItemStack> itemStackCollection) {
        for (ItemStack itemStack : itemStackCollection) {
            if (!itemStack.isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Tests whether one Itemstack is equal to another except size
     *
     * @return false if not equal or any is empty
     */
    public static boolean areItemTypesEqual(ItemStack one, ItemStack two) {
        if (!one.isEmpty() && !two.isEmpty()) {
            return ItemStack.areItemsEqual(one, two) && ItemStack.areNbtEqual(one, two);
        }
        return false;
    }

    /**
     * @return side directions relative to given direction
     */
    public static ArrayList<Direction> getSideDirections(Direction of) {
        ArrayList<Direction> sideDirections = new ArrayList<>(5);
        switch (of) {
            case DOWN, UP -> Collections.addAll(sideDirections, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH);
            case EAST, WEST -> Collections.addAll(sideDirections, Direction.DOWN, Direction.UP, Direction.SOUTH, Direction.NORTH);
            case SOUTH, NORTH -> Collections.addAll(sideDirections, Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST);
        }
        return sideDirections;
    }

    /**
     * Gets a direction of where a signal is coming from
     *
     * @param pulsePosition signal source position
     * @param target        signal target
     * @param notifier      signal source
     */
    public static Direction getPowerIncomingDirection(BlockPos pulsePosition, BlockPos target, Block notifier, World world) {
        BlockState source = world.getBlockState(pulsePosition);
        for (Direction value : Direction.values()) {
            BlockPos sidePos = target.offset(value);
            if (sidePos.equals(pulsePosition)) {
                BlockState sideState = world.getBlockState(sidePos);
                if (sideState == source && notifier == sideState.getBlock())
                    if (sideState.getWeakRedstonePower(world, sidePos, value) > 0)
                        return value;
            }
        }
        return null;
    }

    /**
     * Retrieves specified field from class. Searches superclasses if not found.
     */
    public static Field getSecureField(Class<?> owner, int number) {
        Field f;
        Field[] fields = owner.getDeclaredFields();
        if (number < fields.length) {
            f = fields[number];
            if (f.getType() != owner.getEnclosingClass()) {
                f.setAccessible(true);
                return f;
            } else return getSecureField(owner.getSuperclass(), number);
        }
        return null;
    }

    /**
     * Tests whether an object is a subclass or an instance of specified Class
     *
     * @param classs class to check
     */
    public static boolean isSuperClass(Object instance, Class<?> classs) {
        if (instance == null)
            return false;
        if (instance.getClass() == Class.class)
            return classs.isAssignableFrom((Class<?>) instance);
        return classs.isInstance(instance);
    }

    /**
     * Discovers positions of connected block entities of the specified class or interface, non-recursively
     *
     * @param type class or interface of the block entity
     */
    public static HashSet<BlockPos> getConnectedPositions(Class<?> type, BlockPos start, World world, int limit) {
        HashSet<BlockPos> nextBlocks = new HashSet<>();
        nextBlocks.add(start);
        HashSet<BlockPos> connected = new HashSet<>();
        HashSet<BlockPos> toAdd = new HashSet<>(6);
        boolean removedStart = false;
        while (connected.size() < limit) {
            for (BlockPos nextBlock : nextBlocks) {
                toAdd.addAll(Stream.of(nextBlock.east(), nextBlock.west(), nextBlock.up(), nextBlock.down(), nextBlock.south(), nextBlock.north())
                        .filter(blockPos -> Functions.isSuperClass(world.getBlockEntity(blockPos), type)).collect(Collectors.toSet()));
            }
            if (toAdd.isEmpty())
                break;
            if (!removedStart) {
                removedStart = nextBlocks.remove(start);
            }
            nextBlocks.addAll(toAdd);
            nextBlocks.removeAll(connected);
            connected.addAll(toAdd);
            toAdd.clear();
        }
        return connected;
    }

    /**
     * @return whether the item was fully inserted
     */
    public static boolean insertInto(Inventory inventory, ItemStack item) {
        if (inventory instanceof ChestBlockEntity chestBlockEntity) {
            inventory = ChestBlock.getInventory((ChestBlock) chestBlockEntity.getCachedState().getBlock(), chestBlockEntity.getCachedState(), chestBlockEntity.getWorld(), chestBlockEntity.getPos(), true);
        }
        if (inventory != null) {
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.isValid(i, item)) {
                    ItemStack next = inventory.getStack(i);
                    int count = item.getCount();
                    int nextCount = next.getCount();
                    if (Functions.areItemTypesEqual(item, next) && nextCount < next.getMaxCount() && nextCount + count <= next.getMaxCount()) {
                        next.increment(count);
                        item.decrement(count);
                        return true;
                    }
                }
            }
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.getStack(i).isEmpty() && inventory.isValid(i, item)) {
                    inventory.setStack(i, item);
                    return true;
                }
            }
        }
        return false;
    }
}
