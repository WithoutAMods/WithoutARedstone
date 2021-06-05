package withoutaname.mods.withoutaredstone.data;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;

import withoutaname.mods.withoutaredstone.blocks.WirelessLinkTile;

public interface IFrequencyPowers {
	
	HashMap<Long, HashMap<BlockPos, Integer>> getFrequencyPowers();
	
	void setFrequencyPowers(HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers);
	
	int getPower(long frequency);
	
	void removePower(long frequency, BlockPos pos);
	
	void setPower(long frequency, BlockPos pos, int power);
	
	void notifyReceivers(long frequency);
	
	void addReceiver(long frequency, WirelessLinkTile tile);
	
	void removeReceiver(long frequency, WirelessLinkTile tile);
	
}
