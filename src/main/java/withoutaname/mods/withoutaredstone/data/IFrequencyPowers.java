package withoutaname.mods.withoutaredstone.data;

import net.minecraft.core.BlockPos;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkEntity;

import java.util.HashMap;

public interface IFrequencyPowers {
	
	HashMap<Long, HashMap<BlockPos, Integer>> getFrequencyPowers();
	
	void setFrequencyPowers(HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers);
	
	int getPower(long frequency);
	
	void removePower(long frequency, BlockPos pos);
	
	void setPower(long frequency, BlockPos pos, int power);
	
	void notifyReceivers(long frequency);
	
	void addReceiver(long frequency, WirelessLinkEntity tile);
	
	void removeReceiver(long frequency, WirelessLinkEntity tile);
	
}
