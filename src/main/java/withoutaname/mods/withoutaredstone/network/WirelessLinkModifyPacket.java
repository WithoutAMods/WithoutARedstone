package withoutaname.mods.withoutaredstone.network;

import java.util.HashMap;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import withoutaname.mods.withoutaredstone.blocks.WirelessLinkTile;
import withoutaname.mods.withoutaredstone.gui.WirelessLinkModifyScreen;

public class WirelessLinkModifyPacket {
	
	private static final HashMap<ServerPlayerEntity, WirelessLinkTile> openGUIs = new HashMap<>();
	
	private final long frequency;
	private final boolean receiver;
	
	public WirelessLinkModifyPacket(ServerPlayerEntity player, WirelessLinkTile tile) {
		openGUIs.put(player, tile);
		frequency = tile.getFrequency();
		receiver = tile.isReceiver();
	}
	
	public WirelessLinkModifyPacket(long frequency, boolean receiver) {
		this.frequency = frequency;
		this.receiver = receiver;
	}
	
	public WirelessLinkModifyPacket(@Nonnull PacketBuffer packetBuffer) {
		this(packetBuffer.readLong(), packetBuffer.readBoolean());
	}
	
	public void toBytes(@Nonnull PacketBuffer packetBuffer) {
		packetBuffer.writeLong(frequency);
		packetBuffer.writeBoolean(receiver);
	}
	
	public boolean handle(@Nonnull Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
				WirelessLinkModifyScreen.open(frequency, receiver);
			} else {
				ServerPlayerEntity sender = ctx.get().getSender();
				WirelessLinkTile tile = openGUIs.remove(sender);
				tile.setFrequency(frequency);
				tile.setReceiver(receiver);
			}
		});
		return true;
	}
	
}
