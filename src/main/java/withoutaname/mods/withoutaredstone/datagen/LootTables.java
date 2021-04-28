package withoutaname.mods.withoutaredstone.datagen;

import net.minecraft.data.DataGenerator;

import withoutaname.mods.withoutalib.datagen.BaseLootTableProvider;
import withoutaname.mods.withoutaredstone.setup.Registration;

public class LootTables extends BaseLootTableProvider {
	
	public LootTables(DataGenerator dataGeneratorIn) {
		super(dataGeneratorIn);
	}
	
	@Override
	protected void addTables() {
		createStandardTable(Registration.WIRELESS_LINK_ITEM.get());
	}
	
}
