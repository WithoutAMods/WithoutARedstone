package withoutaname.mods.withoutaredstone.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import withoutaname.mods.withoutaredstone.data.CapabilityFrequencyPowers;
import withoutaname.mods.withoutaredstone.setup.Registration;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WirelessLinkTile extends TileEntity {
	
	private long frequency = 0;
	private boolean receiver = false;
	
	public WirelessLinkTile() {
		super(Registration.WIRELESS_LINK_TILE.get());
	}
	
	public WirelessLinkTile(boolean receiver) {
		this();
		this.receiver = receiver;
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
		assert level != null;
		int power = level.getBestNeighborSignal(worldPosition);
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(WirelessLinkBlock.POWER, power));
		return power;
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
	public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
		super.load(state, nbt);
		frequency = nbt.getLong("frequency");
		receiver = nbt.getBoolean("receiver");
	}
	
	@Nonnull
	@Override
	public CompoundNBT save(@Nonnull CompoundNBT compound) {
		CompoundNBT nbt = super.save(compound);
		nbt.putLong("frequency", frequency);
		nbt.putBoolean("receiver", receiver);
		return nbt;
	}
	
}
