package withoutaname.mods.withoutaredstone.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import withoutaname.mods.withoutaredstone.WithoutARedstone;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkBlock;
import withoutaname.mods.withoutaredstone.setup.Registration;

public class BlockStates extends BlockStateProvider {
	
	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, WithoutARedstone.MODID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		ModelFile wirelessLinkTemplateModel = models().withExistingParent("block/wireless_link", mcLoc("block/block"))
				.ao(false)
				.texture("side", mcLoc("block/smooth_stone"))
				.texture("particle", "#top")
				.element().from(0, 0, 0).to(16, 2, 16)
					.face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").end()
					.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#side").cullface(Direction.DOWN).end()
					.face(Direction.NORTH).uvs(0, 14, 16, 16).texture("#side").cullface(Direction.NORTH).end()
					.face(Direction.SOUTH).uvs(0, 14, 16, 16).texture("#side").cullface(Direction.SOUTH).end()
					.face(Direction.EAST).uvs(0, 14, 16, 16).texture("#side").cullface(Direction.EAST).end()
					.face(Direction.WEST).uvs(0, 14, 16, 16).texture("#side").cullface(Direction.WEST).end()
					.end()
				.element().from(7, 7, 7).to(9, 7, 9)
					.face(Direction.UP).uvs(7, 6, 9, 8).texture("#torch").end()
					.end()
				.element().from(6, 2, 7).to(10, 8, 9)
					.face(Direction.NORTH).uvs(6, 5, 10, 11).texture("#torch").end()
					.face(Direction.SOUTH).uvs(6, 5, 10, 11).texture("#torch").end()
					.end()
				.element().from(7, 2, 6).to(9, 8, 10)
					.face(Direction.EAST).uvs(6, 5, 10, 11).texture("#torch").end()
					.face(Direction.WEST).uvs(6, 5, 10, 11).texture("#torch").end()
					.end();

		ModelFile wirelessLinkReceiverOff = models().getBuilder("block/wireless_link_receiver_off")
				.parent(wirelessLinkTemplateModel)
				.texture("top", modLoc("block/wireless_link"))
				.texture("torch", mcLoc("block/redstone_torch_off"));
		ModelFile wirelessLinkReceiverOn = models().getBuilder("block/wireless_link_receiver_on")
				.parent(wirelessLinkTemplateModel)
				.texture("top", modLoc("block/wireless_link_on"))
				.texture("torch", mcLoc("block/redstone_torch_off"));
		ModelFile wirelessLinkSenderOff = models().getBuilder("block/wireless_link_sender_off")
				.parent(wirelessLinkTemplateModel)
				.texture("top", modLoc("block/wireless_link"))
				.texture("torch", mcLoc("block/redstone_torch"));
		ModelFile wirelessLinkSenderOn = models().getBuilder("block/wireless_link_sender_on")
				.parent(wirelessLinkTemplateModel)
				.texture("top", modLoc("block/wireless_link_on"))
				.texture("torch", mcLoc("block/redstone_torch"));
		
		getVariantBuilder(Registration.WIRELESS_LINK_BLOCK.get()).forAllStates(blockState -> {
			if (blockState.getValue(WirelessLinkBlock.RECEIVER)) {
				return ConfiguredModel.builder()
						.modelFile(blockState.getValue(WirelessLinkBlock.POWER) == 0 ? wirelessLinkReceiverOff : wirelessLinkReceiverOn)
						.build();
			} else {
				return ConfiguredModel.builder()
						.modelFile(blockState.getValue(WirelessLinkBlock.POWER) == 0 ? wirelessLinkSenderOff : wirelessLinkSenderOn)
						.build();
			}
		});
		
		simpleBlockItem(Registration.WIRELESS_LINK_BLOCK.get(), wirelessLinkReceiverOff);
	}
	
}
