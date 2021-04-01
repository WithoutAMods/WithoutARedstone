package withoutaname.mods.withoutaredstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WirelessLinkBlock extends Block {

	public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
	public static final BooleanProperty RECEIVER = BooleanProperty.create("receiver");

	public WirelessLinkBlock() {
		super(Properties.create(Material.MISCELLANEOUS)
				.sound(SoundType.WOOD)
				.zeroHardnessAndResistance());
		this.setDefaultState(this.getStateContainer().getBaseState()
				.with(POWER, 0)
				.with(RECEIVER, false));
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new WirelessLinkTile();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POWER, RECEIVER);
	}

	@Override
	public boolean canProvidePower(@Nonnull BlockState state) {
		return state.isIn(this) && !state.get(RECEIVER);
	}

	@Override
	public int getWeakPower(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return canProvidePower(blockState) ? blockState.get(POWER) : 0;
	}

	@Override
	public int getStrongPower(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return canProvidePower(blockState) ? blockState.get(POWER) : 0;
	}
}
