package me.thetechnicboy.redstonefeatures.block;

import me.thetechnicboy.redstonefeatures.container.RedstoneRequesterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;

public class RedstoneRequesterEntity extends BlockEntity implements MenuProvider  {

    private URL url = null;
    private int lastRedstoneStrength = -1;

    public RedstoneRequesterEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.REDSTONE_REQUESTER_BLOCKENTITY.get(), pos, state);
    }


    public void tick(Level level, BlockPos pos, BlockState state){
        if(!level.isClientSide()) {

            int redstoneStrength = level.getBestNeighborSignal(pos);
            if(lastRedstoneStrength != redstoneStrength) {
                System.out.println("Redstone power: " + redstoneStrength);
                System.out.println("URL: " + getText());
                lastRedstoneStrength = redstoneStrength;
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.redstonefeatures.redstone_requester.title");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new RedstoneRequesterMenu(id, inv, this.worldPosition, getText() );
    }

    public void setText(String text) {
        try{
            url = new URL(text);
        } catch (MalformedURLException e) {
            url = null;
        }
        setChanged();
    }

    public String getText() {
        if(url == null) return "";
        return url.toString();
    }

    @Override
    protected void saveAdditional(CompoundTag tag){
        super.saveAdditional(tag);
        tag.putString("Text", getText());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        setText(tag.getString("Text"));
    }


}
