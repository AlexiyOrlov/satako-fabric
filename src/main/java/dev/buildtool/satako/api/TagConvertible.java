package dev.buildtool.satako.api;

import net.minecraft.nbt.NbtCompound;

public interface TagConvertible<P> {
    NbtCompound writeToTag();

    P readFromTag(NbtCompound nbtCompound);
}
