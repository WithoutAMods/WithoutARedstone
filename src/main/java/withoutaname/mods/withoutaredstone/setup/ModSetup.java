package withoutaname.mods.withoutaredstone.setup;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import withoutaname.mods.withoutaredstone.WithoutARedstone;
import withoutaname.mods.withoutaredstone.data.CapabilityFrequencyPowers;
import withoutaname.mods.withoutaredstone.network.Networking;

public class ModSetup {
	
	public static final ItemGroup defaultItemGroup = new ItemGroup(WithoutARedstone.MODID) {
		
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Registration.WIRELESS_LINK_ITEM.get());
		}
		
	};
	public static final Item.Properties defaultItemProperties = new Item.Properties().tab(defaultItemGroup);
	
	public static void init(FMLCommonSetupEvent event) {
		Networking.registerMessages();
		
		CapabilityFrequencyPowers.register();
	}
	
}
