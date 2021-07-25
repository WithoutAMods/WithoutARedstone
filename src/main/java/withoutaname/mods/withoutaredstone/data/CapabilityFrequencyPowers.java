package withoutaname.mods.withoutaredstone.data;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityFrequencyPowers {
	
	@CapabilityInject(IFrequencyPowers.class)
	public static Capability<IFrequencyPowers> FREQUENCY_POWERS_CAPABILITY = null;
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IFrequencyPowers.class);
		
		MinecraftForge.EVENT_BUS.addGenericListener(Level.class, FrequencyPowersEventHandler::onAttachCapabilitiesEvent);
	}
	
}
