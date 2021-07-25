package withoutaname.mods.withoutaredstone.network;

import java.util.HashMap;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkEntity;
import withoutaname.mods.withoutaredstone.gui.WirelessLinkModifyScreen;

public class WirelessLinkModifyPacket {
	
	private static final HashMap<ServerPlayer, WirelessLinkEntity> openGUIs = new HashMap<>();
	
	private final long frequency;
	private final boolean receiver;
	
	public WirelessLinkModifyPacket(ServerPlayer player, WirelessLinkEntity tile) {
		openGUIs.put(player, tile);
		frequency = tile.getFrequency();
		receiver = tile.isReceiver();
	}
	
	public WirelessLinkModifyPacket(long frequency, boolean receiver) {
		this.frequency = frequency;
		this.receiver = receiver;
	}
	
	public WirelessLinkModifyPacket(@Nonnull FriendlyByteBuf packetBuffer) {
		this(packetBuffer.readLong(), packetBuffer.readBoolean());
	}
	
	public void toBytes(@Nonnull FriendlyByteBuf packetBuffer) {
		packetBuffer.writeLong(frequency);
		packetBuffer.writeBoolean(receiver);
	}
	
	public boolean handle(@Nonnull Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
				WirelessLinkModifyScreen.open(frequency, receiver);
			} else {
				ServerPlayer sender = ctx.get().getSender();
				WirelessLinkEntity tile = openGUIs.remove(sender);
				tile.setFrequency(frequency);
				tile.setReceiver(receiver);
			}
		});
		return true;
	}
	
}
