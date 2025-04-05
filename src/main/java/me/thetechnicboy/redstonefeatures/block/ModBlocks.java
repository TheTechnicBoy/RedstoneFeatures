package me.thetechnicboy.redstonefeatures.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static me.thetechnicboy.redstonefeatures.Redstonefeatures.MODID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }

    public static final RegistryObject<Block> REDSTONE_REQUESTER = BLOCKS.register("redstone_requester", RedstoneRequester::new);

    public static final RegistryObject<BlockEntityType<RedstoneRequesterEntity>> REDSTONE_REQUESTER_BLOCKENTITY = BLOCK_ENTITIES.register("redstone_requester",
            () -> BlockEntityType.Builder.of(RedstoneRequesterEntity::new, ModBlocks.REDSTONE_REQUESTER.get()).build(null));
}
