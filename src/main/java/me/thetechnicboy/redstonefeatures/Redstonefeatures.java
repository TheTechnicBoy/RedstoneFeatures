package me.thetechnicboy.redstonefeatures;

import com.mojang.logging.LogUtils;
import me.thetechnicboy.redstonefeatures.block.ModBlocks;
import me.thetechnicboy.redstonefeatures.container.ModMenus;
import me.thetechnicboy.redstonefeatures.container.RedstoneRequesterScreen;
import me.thetechnicboy.redstonefeatures.item.ModItems;
import me.thetechnicboy.redstonefeatures.packets.ModPackets;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Redstonefeatures.MODID)
public class Redstonefeatures {

    public static ResourceLocation genRL(String key) { return ResourceLocation.tryBuild(MODID, key); }

    public static final String MODID = "redstonefeatures";
    private static final Logger LOGGER = LogUtils.getLogger();



    public Redstonefeatures() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onClientSetup);
        //modEventBus.addListener(this::addCreative);

        modEventBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) event.accept(ModItems.REDSTONE_REQUESTER);
        });

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenus.register(modEventBus);
        ModPackets.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        MenuScreens.register(ModMenus.REDSTONE_REQUESTER_MENU.get(), RedstoneRequesterScreen::new);

    }
}
