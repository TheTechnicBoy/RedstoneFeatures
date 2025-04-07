package me.thetechnicboy.redstonefeatures.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class RedstoneRequesterMenu extends AbstractContainerMenu {

    public final String mURL;
    public final BlockPos mPos;


    public RedstoneRequesterMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos(), buf.readUtf());
    }

    public RedstoneRequesterMenu(int id, Inventory inv, BlockPos pos, String url) {
        super(ModMenus.REDSTONE_REQUESTER_MENU.get(), id);
        mPos = pos;
        mURL = url;

        addPlayerInventory(inv);
    }

    private void addPlayerInventory(Inventory inv) {
        int startX = 8;    // X-Startposition, anpassen an dein GUI-Layout
        int startY = 84;   // Y-Startposition für das Hauptinventar
        int slotSize = 18; // Standard Slot-Größe

        // Hinzufügen des Spielerhauptinventars (3 Reihen à 9 Slots)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = col + row * 9 + 9;  // Der erste Slot des Hauptinventars beginnt bei Index 9
                int x = startX + col * slotSize;
                int y = startY + row * slotSize;
                this.addSlot(new Slot(inv, slotIndex, x, y));
            }
        }

        // Hinzufügen der Hotbar (1 Reihe à 9 Slots)
        int hotbarY = startY + 3 * slotSize + 4; // 4 Pixel Abstand als Beispiel
        for (int col = 0; col < 9; col++) {
            int x = startX + col * slotSize;
            this.addSlot(new Slot(inv, col, x, hotbarY));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
