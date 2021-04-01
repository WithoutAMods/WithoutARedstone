package withoutaname.mods.withoutaredstone.setup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import withoutaname.mods.withoutaredstone.WithoutARedstone;

public class ModSetup {

	public static final ItemGroup defaultItemGroup = new ItemGroup(WithoutARedstone.MODID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(Registration.WIRELESS_LINK_ITEM.get());
		}

	};

	public static void init(FMLCommonSetupEvent event) {
	}

	public static final Item.Properties defaultItemProperties = new Item.Properties().group(defaultItemGroup);

}
