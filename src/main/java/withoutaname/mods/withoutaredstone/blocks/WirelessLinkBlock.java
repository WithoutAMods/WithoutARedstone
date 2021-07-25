package withoutaname.mods.withoutaredstone.blocks;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import withoutaname.mods.withoutaredstone.network.Networking;
import withoutaname.mods.withoutaredstone.network.WirelessLinkModifyPacket;

public class WirelessLinkBlock extends BaseEntityBlock {
	
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
	
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new WirelessLinkEntity(pos, state);
	}
	
	@Override
	public void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide && !state.getValue(RECEIVER)) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof WirelessLinkEntity) {
				((WirelessLinkEntity) entity).updateSender();
			}
		}
	}
	
	@Override
	public void tick(@Nonnull BlockState state, @Nonnull ServerLevel worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
		BlockEntity entity = worldIn.getBlockEntity(pos);
		if (entity instanceof WirelessLinkEntity) {
			if (state.getValue(RECEIVER)) {
				((WirelessLinkEntity) entity).updateReceiver();
			} else {
				((WirelessLinkEntity) entity).updateSender();
			}
		}
	}
	
	@Nonnull
	@Override
	public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit) {
		if (!level.isClientSide) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof WirelessLinkEntity) {
				ServerPlayer serverPlayer = (ServerPlayer) player;
				Networking.sendToClient(new WirelessLinkModifyPacket(serverPlayer, ((WirelessLinkEntity) entity)), serverPlayer);
			} else {
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!(newState.getBlock() instanceof WirelessLinkBlock) && !level.isClientSide) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof WirelessLinkEntity) {
				((WirelessLinkEntity) entity).blockDestroyed();
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override
	protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWER, RECEIVER);
	}
	
	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState pState) {
		return RenderShape.MODEL;
	}
	
	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return Block.box(0, 0, 0, 16, 2, 16);
	}
	
	@Override
	public boolean isSignalSource(@Nonnull BlockState state) {
		return true;
	}
	
	@Override
	public int getSignal(@Nonnull BlockState blockState, @Nonnull BlockGetter blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return isSignalSource(blockState) ? blockState.getValue(POWER) : 0;
	}
	
	@Override
	public int getDirectSignal(@Nonnull BlockState blockState, @Nonnull BlockGetter blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return isSignalSource(blockState) ? blockState.getValue(POWER) : 0;
	}
	
}
