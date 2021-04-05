package withoutaname.mods.withoutaredstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import withoutaname.mods.withoutaredstone.data.CapabilityFrequencyPowers;
import withoutaname.mods.withoutaredstone.setup.Registration;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WirelessLinkTile extends TileEntity {

	private int frequency = 0;
	private boolean receiver = false;

	public WirelessLinkTile() {
		super(Registration.WIRELESS_LINK_TILE.get());
	}

	public WirelessLinkTile(boolean receiver) {
		this();
		this.receiver = receiver;
		markDirty();
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		assert world != null;
		if (!world.isRemote) {
			int oldFrequency = this.frequency;
			this.frequency = frequency;
			if (receiver) {
				if (world.isBlockLoaded(pos)) {
					world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
							.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(oldFrequency, this));
					world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
							.ifPresent(iFrequencyPowers -> iFrequencyPowers.addReceiver(frequency, this));
					updateReceiver();
				}
			} else {
				world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> {
							HashMap<Integer, HashMap<BlockPos, Integer>> frequencyPowers = iFrequencyPowers.getFrequencyPowers();
							if (frequencyPowers.containsKey(oldFrequency)) {
								frequencyPowers.get(oldFrequency).remove(pos);
								iFrequencyPowers.notifyReceivers(oldFrequency);
							}
							updateSender();
						});
			}
			markDirty();
		}
	}

	public boolean isReceiver() {
		return receiver;
	}

	public void setReceiver(boolean receiver) {
		assert world != null;
		if (!world.isRemote && world.isBlockLoaded(pos)) {
			world.setBlockState(pos, getBlockState().with(WirelessLinkBlock.RECEIVER, receiver));
			this.receiver = receiver;
			if (receiver) {
				world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> {
							HashMap<Integer, HashMap<BlockPos, Integer>> frequencyPowers = iFrequencyPowers.getFrequencyPowers();
							if (frequencyPowers.containsKey(frequency)) {
								frequencyPowers.get(frequency).remove(pos);
							}
							iFrequencyPowers.addReceiver(frequency, this);
						});
				updateReceiver();
			} else {
				world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(frequency, this));
				updateSender();
			}
			markDirty();
		}
	}

	public void updateReceiver() {
		if (world != null && !world.isRemote) {
			AtomicInteger power = new AtomicInteger();
			world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
					.ifPresent(iFrequencyPowers -> power.set(iFrequencyPowers.getPower(frequency)));
			world.setBlockState(pos, getBlockState().with(WirelessLinkBlock.POWER, power.get()));
		}
	}

	public void updateSender() {
		if (world != null && !world.isRemote) {
			world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
					.ifPresent(iFrequencyPowers -> iFrequencyPowers.setPower(frequency, pos, getPower()));
		}
	}

	private int getPower() {
		return Math.max(Math.max(
				Math.max(getPowerOnSide(Direction.NORTH), getPowerOnSide(Direction.SOUTH)),
				Math.max(getPowerOnSide(Direction.EAST), getPowerOnSide(Direction.WEST))),
				Math.max(getPowerOnSide(Direction.UP), getPowerOnSide(Direction.DOWN)));
	}

	private int getPowerOnSide(Direction side) {
		assert world != null;
		BlockPos blockPos = pos.offset(side);
		BlockState blockstate = world.getBlockState(blockPos);
		Block block = blockstate.getBlock();
		if (blockstate.canProvidePower()) {
			if (block == Blocks.REDSTONE_BLOCK) {
				return 15;
			} else {
				return block == Blocks.REDSTONE_WIRE ? blockstate.get(RedstoneWireBlock.POWER) : world.getStrongPower(blockPos, side);
			}
		} else {
			return 0;
		}
	}

	@Override
	public void onLoad() {
		assert world != null;
		if (!world.isRemote) {
			if (receiver) {
				world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> iFrequencyPowers.addReceiver(frequency, this));
			}
			world.getPendingBlockTicks().scheduleTick(pos, Registration.WIRELESS_LINK_BLOCK.get(), 0);
		}
	}

	@Override
	public void onChunkUnloaded() {
		assert world != null;
		if (!world.isRemote) {
			world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
					.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(frequency, this));
		}
	}

	public void blockDestroyed() {
		assert world != null;
		if (!world.isRemote) {
			if (receiver) {
				world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> iFrequencyPowers.removeReceiver(frequency, this));
			} else {
				world.getCapability(CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY)
						.ifPresent(iFrequencyPowers -> {
							HashMap<Integer, HashMap<BlockPos, Integer>> frequencyPowers = iFrequencyPowers.getFrequencyPowers();
							if (frequencyPowers.containsKey(frequency)) {
								frequencyPowers.get(frequency).remove(pos);
								iFrequencyPowers.notifyReceivers(frequency);
							}
						});
			}
		}
	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
		super.read(state, nbt);
		frequency = nbt.getInt("frequency");
		receiver = nbt.getBoolean("receiver");
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound) {
		CompoundNBT nbt = super.write(compound);
		nbt.putInt("frequency", frequency);
		nbt.putBoolean("receiver", receiver);
		return nbt;
	}
}
