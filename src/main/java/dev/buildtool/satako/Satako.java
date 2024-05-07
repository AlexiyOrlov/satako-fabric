package dev.buildtool.satako;

import dev.buildtool.satako.test.TestBlock;
import dev.buildtool.satako.test.TestScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Satako implements ModInitializer {
    static String ID = "satako";
    public static final ExtendedScreenHandlerType<TestScreenHandler> testScreenHandlerType = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new TestScreenHandler(syncId, inventory));

    @Override
    public void onInitialize() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(ID, "test_handler"), testScreenHandlerType);
        Block block = Registry.register(Registries.BLOCK, new Identifier(ID, "test_block"), new TestBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.ORANGE)));
        Registry.register(Registries.ITEM, new Identifier(ID, "test_block"), new BlockItem(block, new Item.Settings().maxCount(1)));
    }
}
