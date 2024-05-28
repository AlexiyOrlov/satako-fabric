package dev.buildtool.satako;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.buildtool.satako.test.TestBlock;
import dev.buildtool.satako.test.TestScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class Satako implements ModInitializer {
    static String ID = "satako";
    public static final ExtendedScreenHandlerType<TestScreenHandler> testScreenHandlerType = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new TestScreenHandler(syncId, inventory));

    @Override
    public void onInitialize() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(ID, "test_handler"), testScreenHandlerType);
        Block block = Registry.register(Registry.BLOCK, new Identifier(ID, "test_block"), new TestBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.ORANGE)));
        Registry.register(Registry.ITEM, new Identifier(ID, "test_block"), new BlockItem(block, new Item.Settings().maxCount(1)));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> summon2 = dispatcher.register(CommandManager.literal("summon2").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)));
            SuggestionProvider<ServerCommandSource> namespaces = (context, builder) -> CommandSource.suggestMatching(Registry.ENTITY_TYPE.getIds().stream().map(identifier -> identifier.getNamespace()), builder);
            SuggestionProvider<ServerCommandSource> entities = (context, builder) -> CommandSource.suggestMatching(Registry.ENTITY_TYPE.getIds().stream().map(identifier -> identifier.getPath()), builder);
            RequiredArgumentBuilder<ServerCommandSource, String> namespace = CommandManager.argument("namespace", StringArgumentType.string()).suggests(namespaces);
            RequiredArgumentBuilder<ServerCommandSource, String> entityName = CommandManager.argument("entity", StringArgumentType.string()).suggests(entities);
            entityName.executes(context -> {
                Identifier identifier = new Identifier(context.getArgument("namespace", String.class), context.getArgument("entity", String.class));
                ServerCommandSource serverCommandSource = context.getSource();
                return summonEntity(serverCommandSource, serverCommandSource.getPosition().add(0.5, 0, 0.5), identifier);
            });

            RequiredArgumentBuilder<ServerCommandSource, PosArgument> position = CommandManager.argument("position", Vec3ArgumentType.vec3(true));
            position.executes(context -> {
                Identifier identifier = new Identifier(context.getArgument("namespace", String.class), context.getArgument("entity", String.class));
                ServerCommandSource serverCommandSource = context.getSource();
                Vec3d vec3d = Vec3ArgumentType.getVec3(context, "position");
                return summonEntity(serverCommandSource, vec3d, identifier);
            });

            LiteralCommandNode<ServerCommandSource> built = summon2.createBuilder().build();
            ArgumentCommandNode<ServerCommandSource, String> domain = namespace.build();
            ArgumentCommandNode<ServerCommandSource, String> name = entityName.build();

            built.addChild(domain);
            domain.addChild(name);
            name.addChild(position.build());
            dispatcher.getRoot().addChild(built);
        });
    }

    private static int summonEntity(ServerCommandSource commandSource, Vec3d position, Identifier identifier) {
        ServerWorld serverWorld = commandSource.getWorld();
        Entity entity = Registry.ENTITY_TYPE.get(identifier).create(serverWorld);
        if (entity == null) {
            commandSource.sendFeedback(Text.literal("No entity " + identifier.toString()), false);
            return -1;
        }
        entity.setPosition(position);
        if (entity instanceof MobEntity mob)
            mob.initialize(serverWorld, serverWorld.getLocalDifficulty(entity.getBlockPos()), SpawnReason.COMMAND, null, null);
        serverWorld.spawnEntity(entity);
        commandSource.sendFeedback(Text.literal("Summoned " + entity.getName() + " at " + (int) position.x + " " + (int) position.y + " " + (int) position.z), true);
        return 1;
    }
}
