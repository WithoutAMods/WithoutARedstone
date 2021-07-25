package withoutaname.mods.withoutaredstone.setup;

import javax.annotation.Nonnull;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import withoutaname.mods.withoutaredstone.WithoutARedstone;
import withoutaname.mods.withoutaredstone.data.CapabilityFrequencyPowers;
import withoutaname.mods.withoutaredstone.network.Networking;

public class ModSetup {
	
	public static final CreativeModeTab DEFAULT_CREATIVE_MODE_TAB = new CreativeModeTab(WithoutARedstone.MODID) {
		
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Registration.WIRELESS_LINK_ITEM.get());
		}
		
	};
	public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().tab(DEFAULT_CREATIVE_MODE_TAB);
	
	public static void init(FMLCommonSetupEvent event) {
		Networking.registerMessages();
		
		CapabilityFrequencyPowers.register();
	}
	
}
