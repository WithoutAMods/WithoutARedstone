package withoutaname.mods.withoutaredstone.data;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import withoutaname.mods.withoutaredstone.WithoutARedstone;

public class FrequencyPowersEventHandler {
	
	public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<World> event) {
		FrequencyPowersProvider provider = new FrequencyPowersProvider();
		event.addCapability(new ResourceLocation(WithoutARedstone.MODID, "sections"), provider);
		event.addListener(provider::invalidate);
	}
	
}
