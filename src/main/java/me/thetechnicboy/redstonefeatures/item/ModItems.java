package me.thetechnicboy.redstonefeatures.item;

import me.thetechnicboy.redstonefeatures.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static me.thetechnicboy.redstonefeatures.Redstonefeatures.MODID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> REDSTONE_REQUESTER = ITEMS.register("redstone_requester",
            () -> new BlockItem(ModBlocks.REDSTONE_REQUESTER.get(), new Item.Properties()));
}
