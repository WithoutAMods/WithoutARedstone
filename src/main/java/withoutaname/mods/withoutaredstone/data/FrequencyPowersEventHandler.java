package withoutaname.mods.withoutaredstone.data;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import withoutaname.mods.withoutaredstone.WithoutARedstone;

public class FrequencyPowersEventHandler {
	
	public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Level> event) {
		FrequencyPowersProvider provider = new FrequencyPowersProvider();
		event.addCapability(new ResourceLocation(WithoutARedstone.MODID, "sections"), provider);
		event.addListener(provider::invalidate);
	}
	
}
