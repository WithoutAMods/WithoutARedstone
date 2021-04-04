package withoutaname.mods.withoutaredstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

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
	public TileEntity createTileEntity(@Nonnull BlockState state, IBlockReader world) {
		return new WirelessLinkTile(state.get(RECEIVER));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
		if (!state.get(RECEIVER)) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof WirelessLinkTile) {
				((WirelessLinkTile) te).updateSender();
			}
		}
	}

	@Override
	public void tick(@Nonnull BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof WirelessLinkTile) {
			if (state.get(RECEIVER)) {
				((WirelessLinkTile) te).updateReceiver();
			} else {
				((WirelessLinkTile) te).updateSender();
			}
		}
	}

	@Nonnull
	@Override
	public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof WirelessLinkTile) {
				((WirelessLinkTile) te).setReceiver(!state.get(RECEIVER));
			} else {
				return ActionResultType.FAIL;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!(newState.getBlock() instanceof WirelessLinkBlock) && !worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof WirelessLinkTile) {
				((WirelessLinkTile) te).blockDestroyed();
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	@Override
	protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POWER, RECEIVER);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return true;
	}

	@Override
	public boolean canProvidePower(@Nonnull BlockState state) {
		return state.isIn(this) && state.get(RECEIVER);
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
