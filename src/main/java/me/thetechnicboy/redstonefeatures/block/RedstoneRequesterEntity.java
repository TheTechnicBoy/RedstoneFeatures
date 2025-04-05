package me.thetechnicboy.redstonefeatures.block;

import com.google.gson.JsonObject;
import me.thetechnicboy.redstonefeatures.container.RedstoneRequesterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RedstoneRequesterEntity extends BlockEntity implements MenuProvider  {

    private URL url = null;
    private int lastRedstoneStrength = -1;

    public RedstoneRequesterEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.REDSTONE_REQUESTER_BLOCKENTITY.get(), pos, state);
    }

    int tickCounter = 0;
    public void tick(Level level, BlockPos pos, BlockState state){
        tickCounter++;
        if(tickCounter == 2) {
            tickCounter = 0;
            if (!level.isClientSide()) {
                int redstoneStrength = level.getBestNeighborSignal(pos);
                if (lastRedstoneStrength != redstoneStrength) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("type", "redstone_requester");
                    JsonObject data = new JsonObject();
                    data.addProperty("redstoneStrength", redstoneStrength);
                    data.addProperty("posX", pos.get(Direction.Axis.X));
                    data.addProperty("posY", pos.get(Direction.Axis.Y));
                    data.addProperty("posZ", pos.get(Direction.Axis.Z));
                    obj.add("data", data);
                    postRequest(obj.toString());
                    lastRedstoneStrength = redstoneStrength;
                }
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

    private void postRequest(String data){
        try{
            if(url == null) return;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (InputStream is = connection.getInputStream()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (IOException e) {}
    }

}
