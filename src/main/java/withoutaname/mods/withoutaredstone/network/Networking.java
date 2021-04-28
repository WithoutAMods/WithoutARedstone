package withoutaname.mods.withoutaredstone.network;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import withoutaname.mods.withoutaredstone.WithoutARedstone;

public class Networking {
	
	private static final String VERSION = "1.0";
	private static SimpleChannel INSTANCE;
	private static int ID = 0;
	
	private static int nextID() {
		return ID++;
	}
	
	public static void registerMessages() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(WithoutARedstone.MODID, "withoutaredstone"),
				() -> VERSION,
				VERSION::equals,
				VERSION::equals);
		
		INSTANCE.messageBuilder(WirelessLinkModifyPacket.class, nextID())
				.encoder(WirelessLinkModifyPacket::toBytes)
				.decoder(WirelessLinkModifyPacket::new)
				.consumer(WirelessLinkModifyPacket::handle)
				.add();
	}
	
	public static void sendToClient(Object packet, @Nonnull ServerPlayerEntity player) {
		INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}
	
	public static void sendToServer(Object packet) {
		INSTANCE.sendToServer(packet);
	}
	
}
