package withoutaname.mods.withoutaredstone.data;

import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityFrequencyPowers {
	
	@CapabilityInject(IFrequencyPowers.class)
	public static Capability<IFrequencyPowers> FREQUENCY_POWERS_CAPABILITY = null;
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IFrequencyPowers.class, new Storage(), DefaultFrequencyPowers::new);
		
		MinecraftForge.EVENT_BUS.addGenericListener(World.class, FrequencyPowersEventHandler::onAttachCapabilitiesEvent);
	}
	
	private static class Storage implements Capability.IStorage<IFrequencyPowers> {
		
		@Nullable
		@Override
		public INBT writeNBT(Capability<IFrequencyPowers> capability, @Nonnull IFrequencyPowers instance, Direction side) {
			ListNBT frequencyPowersNBT = new ListNBT();
			instance.getFrequencyPowers().forEach((frequency, posPowers) -> {
				CompoundNBT nbt = new CompoundNBT();
				nbt.putInt("key", frequency);
				nbt.put("value", getValue(posPowers));
				frequencyPowersNBT.add(nbt);
			});
			return frequencyPowersNBT;
		}
		
		@Nonnull
		private ListNBT getValue(@Nonnull HashMap<BlockPos, Integer> posPowers) {
			ListNBT value = new ListNBT();
			posPowers.forEach((pos, power) -> {
				CompoundNBT nbt = new CompoundNBT();
				nbt.put("key", NBTUtil.writeBlockPos(pos));
				nbt.putInt("value", power);
				value.add(nbt);
			});
			return value;
		}
		
		@Override
		public void readNBT(Capability<IFrequencyPowers> capability, IFrequencyPowers instance, Direction side, INBT inbt) {
			HashMap<Integer, HashMap<BlockPos, Integer>> frequencyPowers = new HashMap<>();
			if (inbt instanceof ListNBT) {
				for (INBT nbt : (ListNBT) inbt) {
					if (nbt instanceof CompoundNBT) {
						CompoundNBT compoundNBT = (CompoundNBT) nbt;
						frequencyPowers.put(compoundNBT.getInt("key"), getMap(compoundNBT.get("value")));
					}
				}
			}
			instance.setFrequencyPowers(frequencyPowers);
		}
		
		@Nonnull
		private HashMap<BlockPos, Integer> getMap(INBT inbt) {
			HashMap<BlockPos, Integer> posPowers = new HashMap<>();
			if (inbt instanceof ListNBT) {
				for (INBT nbt : (ListNBT) inbt) {
					if (nbt instanceof CompoundNBT) {
						CompoundNBT compoundNBT = (CompoundNBT) nbt;
						INBT posNBT = compoundNBT.get("key");
						if (posNBT instanceof CompoundNBT) {
							posPowers.put(NBTUtil.readBlockPos((CompoundNBT) posNBT), compoundNBT.getInt("value"));
						}
					}
				}
			}
			return posPowers;
		}
		
	}
	
}
