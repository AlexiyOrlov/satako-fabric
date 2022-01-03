package dev.buildtool.satako.api;

import net.minecraft.nbt.NbtCompound;

public interface TagConvertible {
    NbtCompound writeToTag();

    void readFromTag(NbtCompound nbtCompound);
}
