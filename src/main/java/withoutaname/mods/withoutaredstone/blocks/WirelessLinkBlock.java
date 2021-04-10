package withoutaname.mods.withoutaredstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import withoutaname.mods.withoutaredstone.network.Networking;
import withoutaname.mods.withoutaredstone.network.WirelessLinkModifyPacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class WirelessLinkBlock extends Block {

	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty RECEIVER = BooleanProperty.create("receiver");

	public WirelessLinkBlock() {
		super(Properties.of(Material.DECORATION)
				.sound(SoundType.WOOD)
				.instabreak());
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(POWER, 0)
				.setValue(RECEIVER, false));
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, IBlockReader world) {
		return new WirelessLinkTile(state.getValue(RECEIVER));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isClientSide && !state.getValue(RECEIVER)) {
			TileEntity te = worldIn.getBlockEntity(pos);
			if (te instanceof WirelessLinkTile) {
				((WirelessLinkTile) te).updateSender();
			}
		}
	}

	@Override
	public void tick(@Nonnull BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
		TileEntity te = worldIn.getBlockEntity(pos);
		if (te instanceof WirelessLinkTile) {
			if (state.getValue(RECEIVER)) {
				((WirelessLinkTile) te).updateReceiver();
			} else {
				((WirelessLinkTile) te).updateSender();
			}
		}
	}

	@Nonnull
	@Override
	public ActionResultType use(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
		if (!worldIn.isClientSide) {
			TileEntity te = worldIn.getBlockEntity(pos);
			if (te instanceof WirelessLinkTile) {
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
				Networking.sendToClient(new WirelessLinkModifyPacket(serverPlayer, ((WirelessLinkTile) te)), serverPlayer);
			} else {
				return ActionResultType.FAIL;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!(newState.getBlock() instanceof WirelessLinkBlock) && !worldIn.isClientSide) {
			TileEntity te = worldIn.getBlockEntity(pos);
			if (te instanceof WirelessLinkTile) {
				((WirelessLinkTile) te).blockDestroyed();
			}
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Override
	protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POWER, RECEIVER);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
		return Block.box(0, 0, 0, 16, 2, 16);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
		return true;
	}

	@Override
	public boolean isSignalSource(@Nonnull BlockState state) {
		return state.is(this) && state.getValue(RECEIVER);
	}

	@Override
	public int getSignal(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return isSignalSource(blockState) ? blockState.getValue(POWER) : 0;
	}

	@Override
	public int getDirectSignal(@Nonnull BlockState blockState, @Nonnull IBlockReader blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return isSignalSource(blockState) ? blockState.getValue(POWER) : 0;
	}
}
