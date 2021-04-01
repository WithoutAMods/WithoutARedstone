package withoutaname.mods.withoutaredstone.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import withoutaname.mods.withoutaredstone.WithoutARedstone;

public class BlockStates extends BlockStateProvider {

	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, WithoutARedstone.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
	}

}
