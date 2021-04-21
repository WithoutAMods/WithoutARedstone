package withoutaname.mods.withoutaredstone.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FrequencyPowersProvider implements ICapabilitySerializable<INBT> {

	private final DefaultFrequencyPowers frequencyPowers = new DefaultFrequencyPowers();
	private final LazyOptional<IFrequencyPowers> frequencyPowersOptional = LazyOptional.of(() -> frequencyPowers);

	public void invalidate() {
		frequencyPowersOptional.invalidate();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY) {
			return frequencyPowersOptional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public INBT serializeNBT() {
		if (CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY != null) {
			return CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY.writeNBT(frequencyPowers, null);
		} else {
			return new CompoundNBT();
		}
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		if (CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY != null) {
			CapabilityFrequencyPowers.FREQUENCY_POWERS_CAPABILITY.readNBT(frequencyPowers, null, nbt);
		}
	}
}
