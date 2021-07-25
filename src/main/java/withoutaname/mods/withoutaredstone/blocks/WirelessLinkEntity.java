package withoutaname.mods.withoutaredstone.blocks;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import withoutaname.mods.withoutaredstone.data.CapabilityFrequencyPowers;
import withoutaname.mods.withoutaredstone.setup.Registration;

public class WirelessLinkEntity extends BlockEntity {
	
	private long frequency = 0;
	private boolean receiver = false;
	
	public WirelessLinkEntity(BlockPos pos, BlockState state) {
		super(Registration.WIRELESS_LINK_TILE.get(), pos, state);
		this.receiver = state.getValue(WirelessLinkBlock.RECEIVER);
		setChanged();
	}
	
	public long getFrequency() {
		return frequency;
	}
	
	public void setFrequency(long frequency) {
		assert level != null;
		if (!level.isClientSide) {
			long oldFrequency = this.frequency;
			this.frequency = frequency;
			if (receiver) {
				if (level.hasChunkAt(worldPosition)) {
					level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
							.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(oldFrequency, this));
					level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
							.ifPresent(iFrequencyPowers -> iFrequencyPowers.addReceiver(frequency, this));
					updateReceiver();
				}
			} else {
				level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> {
							HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers = iFrequencyPowers.getFrequencyPowers();
							if (frequencyPowers.containsKey(oldFrequency)) {
								frequencyPowers.get(oldFrequency).remove(worldPosition);
								iFrequencyPowers.notifyReceivers(oldFrequency);
							}
							updateSender();
						});
			}
			setChanged();
		}
	}
	
	public boolean isReceiver() {
		return receiver;
	}
	
	public void setReceiver(boolean receiver) {
		assert level != null;
		if (!level.isClientSide && level.hasChunkAt(worldPosition)) {
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(WirelessLinkBlock.RECEIVER, receiver));
			this.receiver = receiver;
			if (receiver) {
				level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> {
							HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers = iFrequencyPowers.getFrequencyPowers();
							if (frequencyPowers.containsKey(frequency)) {
								frequencyPowers.get(frequency).remove(worldPosition);
							}
							iFrequencyPowers.notifyReceivers(frequency);
							iFrequencyPowers.addReceiver(frequency, this);
						});
				updateReceiver();
			} else {
				level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(frequency, this));
				updateSender();
			}
			setChanged();
		}
	}
	
	public void updateReceiver() {
		if (level != null && !level.isClientSide) {
			AtomicInteger power = new AtomicInteger();
			level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
					.ifPresent(iFrequencyPowers -> power.set(iFrequencyPowers.getPower(frequency)));
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(WirelessLinkBlock.POWER, power.get()));
		}
	}
	
	public void updateSender() {
		if (level != null && !level.isClientSide) {
			level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
					.ifPresent(iFrequencyPowers -> iFrequencyPowers.setPower(frequency, worldPosition, getPower()));
		}
	}
	
	private int getPower() {
		int power = Math.max(Math.max(
				Math.max(getPowerOnSide(Direction.NORTH), getPowerOnSide(Direction.SOUTH)),
				Math.max(getPowerOnSide(Direction.EAST), getPowerOnSide(Direction.WEST))),
				Math.max(getPowerOnSide(Direction.UP), getPowerOnSide(Direction.DOWN)));
		if (level != null) {
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(WirelessLinkBlock.POWER, power));
		}
		return power;
	}
	
	private int getPowerOnSide(Direction side) {
		assert level != null;
		BlockPos blockPos = worldPosition.relative(side);
		BlockState blockstate = level.getBlockState(blockPos);
		Block block = blockstate.getBlock();
		if (blockstate.isSignalSource()) {
			if (block == Blocks.REDSTONE_BLOCK) {
				return 15;
			} else {
				return block == Blocks.REDSTONE_WIRE ? blockstate.getValue(RedStoneWireBlock.POWER) : level.getDirectSignal(blockPos, side);
			}
		} else {
			return 0;
		}
	}
	
	@Override
	public void onLoad() {
		assert level != null;
		if (!level.isClientSide) {
			if (receiver) {
				level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> iFrequencyPowers.addReceiver(frequency, this));
			}
			level.getBlockTicks().scheduleTick(worldPosition, Registration.WIRELESS_LINK_BLOCK.get(), 0);
		}
	}
	
	@Override
	public void onChunkUnloaded() {
		assert level != null;
		if (!level.isClientSide) {
			level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
					.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(frequency, this));
		}
	}
	
	public void blockDestroyed() {
		assert level != null;
		if (!level.isClientSide) {
			if (receiver) {
				level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(frequency, this));
			} else {
				level.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> {
							HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers = iFrequencyPowers.getFrequencyPowers();
							if (frequencyPowers.containsKey(frequency)) {
								frequencyPowers.get(frequency).remove(worldPosition);
								iFrequencyPowers.notifyReceivers(frequency);
							}
						});
			}
		}
	}
	
	@Override
	public void load(@Nonnull CompoundTag tag) {
		super.load(tag);
		frequency = tag.getLong("frequency");
		receiver = tag.getBoolean("receiver");
	}
	
	@Nonnull
	@Override
	public CompoundTag save(@Nonnull CompoundTag compound) {
		CompoundTag tag = super.save(compound);
		tag.putLong("frequency", frequency);
		tag.putBoolean("receiver", receiver);
		return tag;
	}
	
}