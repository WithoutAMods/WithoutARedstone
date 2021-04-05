package withoutaname.mods.withoutaredstone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkTile;
import withoutaname.mods.withoutaredstone.gui.WirelessLinkModifyScreen;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.function.Supplier;

public class WirelessLinkModifyPacket {

	private static final HashMap<ServerPlayerEntity, WirelessLinkTile> openGUIs = new HashMap<>();

	private final int frequency;
	private final boolean receiver;

	public WirelessLinkModifyPacket(ServerPlayerEntity player, WirelessLinkTile tile) {
		openGUIs.put(player, tile);
		frequency = tile.getFrequency();
		receiver = tile.isReceiver();
	}

	public WirelessLinkModifyPacket(int frequency, boolean receiver) {
		this.frequency = frequency;
		this.receiver = receiver;
	}

	public WirelessLinkModifyPacket(@Nonnull PacketBuffer packetBuffer) {
		this(packetBuffer.readInt(), packetBuffer.readBoolean());
	}

	public void toBytes(@Nonnull PacketBuffer packetBuffer) {
		packetBuffer.writeInt(frequency);
		packetBuffer.writeBoolean(receiver);
	}

	public boolean handle(@Nonnull Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
				Minecraft.getInstance().displayGuiScreen(new WirelessLinkModifyScreen(frequency, receiver));
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
