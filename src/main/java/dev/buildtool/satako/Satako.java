package dev.buildtool.satako;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.stream.Collectors;

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

            //give2
            SuggestionProvider<ServerCommandSource> mods = (context, builder) -> CommandSource.suggestMatching(Registry.ITEM.getIds().stream().map(Identifier::getNamespace).collect(Collectors.toSet()).stream(), builder);
            SuggestionProvider<ServerCommandSource> items = (context, builder) -> CommandSource.suggestMatching(Registry.ITEM.getIds().stream().filter(identifier -> identifier.getNamespace().equals(context.getArgument("mod", String.class))).map(Identifier::getPath).collect(Collectors.toSet()).stream(), builder);

            LiteralCommandNode<ServerCommandSource> give2 = dispatcher.register(CommandManager.literal("give2").requires(commandSource -> commandSource.hasPermissionLevel(2)));
            RequiredArgumentBuilder<ServerCommandSource, EntitySelector> targets = CommandManager.argument("targets", EntityArgumentType.players());
            RequiredArgumentBuilder<ServerCommandSource, String> itemMod = CommandManager.argument("mod", StringArgumentType.string()).suggests(mods);
            RequiredArgumentBuilder<ServerCommandSource, String> itemPath = CommandManager.argument("item", StringArgumentType.string()).suggests(items);
            itemPath.executes(context -> giveItems(context, 1));
            RequiredArgumentBuilder<ServerCommandSource, Integer> count = CommandManager.argument("count", IntegerArgumentType.integer(1));
            count.executes(context -> giveItems(context, IntegerArgumentType.getInteger(context, "count")));

            LiteralCommandNode<ServerCommandSource> giveNode = give2.createBuilder().build();
            ArgumentCommandNode<ServerCommandSource, EntitySelector> players = targets.build();
            ArgumentCommandNode<ServerCommandSource, String> itemDomain = itemMod.build();
            ArgumentCommandNode<ServerCommandSource, String> item = itemPath.build();
            ArgumentCommandNode<ServerCommandSource, Integer> countNode = count.build();
            giveNode.addChild(players);
            players.addChild(itemDomain);
            itemDomain.addChild(item);
            item.addChild(countNode);
            dispatcher.getRoot().addChild(giveNode);
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

    private static int giveItems(CommandContext<ServerCommandSource> context, int amount) throws CommandSyntaxException {
        String modName = context.getArgument("mod", String.class);
        String itemName = context.getArgument("item", String.class);
        Identifier identifier = new Identifier(modName, itemName);
        Item item = Registry.ITEM.get(identifier);
        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "targets");
        playerEntities.forEach(serverPlayerEntity -> {
            int i = amount;
            while (i > 0) {
                int j = Math.min(item.getMaxCount(), i);
                i -= j;
                ItemStack itemStack = new ItemStack(item, j);
                boolean flag = serverPlayerEntity.getInventory().insertStack(itemStack);
                if (flag && itemStack.isEmpty()) {
                    itemStack.setCount(1);
                    ItemEntity itemEntity = serverPlayerEntity.dropItem(itemStack, false);
                    if (itemEntity != null) {
                        itemEntity.setDespawnImmediately();
                    }

                    serverPlayerEntity.currentScreenHandler.sendContentUpdates();
                } else {
                    ItemEntity itemEntity = serverPlayerEntity.dropItem(itemStack, false);
                    if (itemEntity != null) {
                        itemEntity.setPickupDelayInfinite();
                        itemEntity.setOwner(serverPlayerEntity.getUuid());
                    }
                }
                serverPlayerEntity.getWorld().playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((serverPlayerEntity.getRandom().nextFloat() - serverPlayerEntity.getRandom().nextFloat()) * 0.7f + 1) * 2);

            }
        });
        return 1;
    }
}
