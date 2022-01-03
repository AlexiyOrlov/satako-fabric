package dev.buildtool.satako;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.Validate;

import java.util.UUID;

/**
 * Created on 4/4/18.
 */
@SuppressWarnings("unused")
public class NBTWriter {

    private final NbtCompound tagCompound;

    public NBTWriter(NbtCompound CompoundTag) {
        Validate.notNull(CompoundTag);
        tagCompound = CompoundTag;
    }

    public NBTWriter setVector(String key, Vec3d vector) {
        tagCompound.putDouble(key + "_x", vector.x);
        tagCompound.putDouble(key + "_y", vector.y);
        tagCompound.putDouble(key + "_z", vector.z);
        return this;
    }

    public NBTWriter setShort(String key, short s) {
        tagCompound.putShort(key, s);
        return this;
    }

    public NBTWriter setByte(String key, byte b) {
        tagCompound.putByte(key, b);
        return this;
    }

    public NBTWriter setLong(String key, long l) {
        tagCompound.putLong(key, l);
        return this;
    }

    public NBTWriter setBoolean(String key, boolean b) {
        tagCompound.putBoolean(key, b);
        return this;
    }

    public NBTWriter setFloat(String key, float f) {
        tagCompound.putFloat(key, f);
        return this;
    }

    public NBTWriter setDouble(String key, double d) {
        tagCompound.putDouble(key, d);
        return this;
    }

    public NBTWriter setString(String key, String s) {
        tagCompound.putString(key, s);
        return this;
    }

    public NBTWriter setTagCompound(String key, NbtCompound compoundTag) {
        tagCompound.put(key, compoundTag);
        return this;
    }

    public NBTWriter setPosition(String key, BlockPos blockPos) {
        tagCompound.putLong(key, blockPos.asLong());
        return this;
    }

    public NBTWriter setUUID(String key, UUID uuid) {
        tagCompound.putUuid(key, uuid);
        return this;
    }

    public NBTWriter setInt(String key, int i) {
        tagCompound.putInt(key, i);
        return this;
    }

    public NbtCompound getResult() {
        return tagCompound;
    }
}
