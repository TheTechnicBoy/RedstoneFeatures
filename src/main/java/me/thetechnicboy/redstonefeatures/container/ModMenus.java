package me.thetechnicboy.redstonefeatures.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static me.thetechnicboy.redstonefeatures.Redstonefeatures.MODID;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    public static final RegistryObject<MenuType<RedstoneRequesterMenu>> REDSTONE_REQUESTER_MENU =
            MENUS.register("redstone_requester_menu",
                    () -> IForgeMenuType.create(RedstoneRequesterMenu::new));
}
