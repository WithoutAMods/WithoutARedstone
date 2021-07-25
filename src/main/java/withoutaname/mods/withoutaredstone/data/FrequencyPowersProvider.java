package withoutaname.mods.withoutaredstone.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;

public class FrequencyPowersProvider implements ICapabilitySerializable<Tag> {
	
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
	public Tag serializeNBT() {
		ListTag frequencyPowersNBT = new ListTag();
		frequencyPowers.getFrequencyPowers().forEach((frequency, posPowers) -> {
			CompoundTag nbt = new CompoundTag();
			nbt.putLong("key", frequency);
			nbt.put("value", getValue(posPowers));
			frequencyPowersNBT.add(nbt);
		});
		return frequencyPowersNBT;
	}
	
	@Nonnull
	private ListTag getValue(@Nonnull HashMap<BlockPos, Integer> posPowers) {
		ListTag value = new ListTag();
		posPowers.forEach((pos, power) -> {
			CompoundTag nbt = new CompoundTag();
			nbt.put("key", NbtUtils.writeBlockPos(pos));
			nbt.putInt("value", power);
			value.add(nbt);
		});
		return value;
	}
	
	@Override
	public void deserializeNBT(Tag tag) {
		HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers = new HashMap<>();
		if (tag instanceof ListTag) {
			for (Tag nbt : (ListTag) tag) {
				if (nbt instanceof CompoundTag compoundTag) {
					frequencyPowers.put(compoundTag.getLong("key"), getMap(compoundTag.get("value")));
				}
			}
		}
		this.frequencyPowers.setFrequencyPowers(frequencyPowers);
	}
	
	@Nonnull
	private HashMap<BlockPos, Integer> getMap(Tag tag) {
		HashMap<BlockPos, Integer> posPowers = new HashMap<>();
		if (tag instanceof ListTag) {
			for (Tag nbt : (ListTag) tag) {
				if (nbt instanceof CompoundTag compoundTag) {
					Tag posNBT = compoundTag.get("key");
					if (posNBT instanceof CompoundTag) {
						posPowers.put(NbtUtils.readBlockPos((CompoundTag) posNBT), compoundTag.getInt("value"));
					}
				}
			}
		}
		return posPowers;
	}
	
}
