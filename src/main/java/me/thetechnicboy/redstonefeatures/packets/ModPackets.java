package me.thetechnicboy.redstonefeatures.packets;

import me.thetechnicboy.redstonefeatures.Redstonefeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id(){
        return packetId++;
    }

    public static void register() {
        ResourceLocation channelName = Redstonefeatures.genRL("main");
        INSTANCE = NetworkRegistry.newSimpleChannel(channelName, () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(id(), TextUpdatePacket.class,
                TextUpdatePacket::encode,
                TextUpdatePacket::decode,
                TextUpdatePacket::handle);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
