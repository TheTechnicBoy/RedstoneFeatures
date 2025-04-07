package me.thetechnicboy.redstonefeatures.packets;

import me.thetechnicboy.redstonefeatures.block.RedstoneRequesterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

public class TextUpdatePacket {

    private final BlockPos pos;
    private final String text;

    public TextUpdatePacket(BlockPos pos, String text) {
        this.pos = pos;
        this.text = text;
    }

    public static void encode(TextUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeUtf(packet.text);
    }

    public static TextUpdatePacket decode(FriendlyByteBuf buffer) {
        return new TextUpdatePacket(buffer.readBlockPos(), buffer.readUtf(32767));
    }

    public static void handle(TextUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                BlockEntity entity = player.level().getBlockEntity(packet.pos);
                if (entity instanceof RedstoneRequesterEntity redstoneEntity) {
                    redstoneEntity.setURL(packet.text);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
