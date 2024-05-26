package dev.buildtool.satako;

import dev.buildtool.satako.test.TestBlock;
import dev.buildtool.satako.test.TestScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Satako implements ModInitializer {
    static String ID = "satako";

    public record Data() {
//        public static final PacketCodec<RegistryByteBuf,Data> CODEC=PacketCodec.tuple(PacketCodecs.)
    }

    public static final ScreenHandlerType<TestScreenHandler> TEST_SCREEN_HANDLER_OBJECT_EXTENDED_SCREEN_HANDLER_TYPE = null;//new ExtendedScreenHandlerType<>((t,p,m)->new TestScreenHandler(t,p),PacketCodecs.BOOL);
//    public static final ExtendedScreenHandlerType<TestScreenHandler> testScreenHandlerType = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new TestScreenHandler(syncId, inventory));

    @Override
    public void onInitialize() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(ID, "test_handler"), TEST_SCREEN_HANDLER_OBJECT_EXTENDED_SCREEN_HANDLER_TYPE);
        Block block = Registry.register(Registries.BLOCK, new Identifier(ID, "test_block"), new TestBlock(AbstractBlock.Settings.copy(Blocks.COMMAND_BLOCK)));
        Registry.register(Registries.ITEM, new Identifier(ID, "test_block"), new BlockItem(block, new Item.Settings().maxCount(1)));
    }
}
