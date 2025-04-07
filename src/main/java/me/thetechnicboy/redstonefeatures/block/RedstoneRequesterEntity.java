package me.thetechnicboy.redstonefeatures.block;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.thetechnicboy.redstonefeatures.Redstonefeatures;
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

    private URL mURL = null;
    public int mLastInput = -1;
    public int mLastOutput = -1;

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
                if (mLastInput != redstoneStrength) {
                    mLastInput = redstoneStrength;

                    JsonObject inOBJ = new JsonObject();
                    JsonObject data = new JsonObject();

                    inOBJ.addProperty("type", "redstone_requester");
                    data.addProperty("redstoneStrength", redstoneStrength);
                    data.addProperty("posX", pos.get(Direction.Axis.X));
                    data.addProperty("posY", pos.get(Direction.Axis.Y));
                    data.addProperty("posZ", pos.get(Direction.Axis.Z));
                    inOBJ.add("data", data);

                    String response = postRequest(inOBJ.toString());
                    Redstonefeatures.LOGGER.info(response);
                    JsonObject outOBJ = (JsonObject) JsonParser.parseString(response);
                    if(outOBJ.has("redstoneStrength")) {
                        mLastOutput = outOBJ.get("redstoneStrength").getAsInt();

                        // Signal, dass sich der Block-Zustand geändert hat
                        level.sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);

                        // Redstone-Komparator benachrichtigen
                        level.updateNeighborsAt(pos, getBlockState().getBlock());
                    }
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
        return new RedstoneRequesterMenu(id, inv, this.worldPosition, getURL().toString() );
    }

    public void setURL(String url) {
        try {
            this.mURL = new URL(url);
        } catch (MalformedURLException e) {}
        setChanged();
    }

    public String getURL() {
        if(mURL == null) return "";
        return mURL.toString();
    }

    @Override
    protected void saveAdditional(CompoundTag tag){
        super.saveAdditional(tag);
        tag.putString("URL", getURL());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        setURL(tag.getString("URL"));
    }

    private String postRequest(String data){
        if(mURL != null) {
            try {
                HttpURLConnection connection = (HttpURLConnection) mURL.openConnection();
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
                    return response.toString();
                }
            } catch (IOException e) {}
        }
        return "{}";
    }


}
