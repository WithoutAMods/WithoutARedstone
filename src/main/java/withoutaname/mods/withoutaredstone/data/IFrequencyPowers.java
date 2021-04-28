package withoutaname.mods.withoutaredstone.data;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;

import withoutaname.mods.withoutaredstone.blocks.WirelessLinkTile;

public interface IFrequencyPowers {
	
	HashMap<Integer, HashMap<BlockPos, Integer>> getFrequencyPowers();
	
	void setFrequencyPowers(HashMap<Integer, HashMap<BlockPos, Integer>> frequencyPowers);
	
	int getPower(int frequency);
	
	void removePower(int frequency, BlockPos pos);
	
	void setPower(int frequency, BlockPos pos, int power);
	
	void notifyReceivers(int frequency);
	
	void addReceiver(int frequency, WirelessLinkTile tile);
	
	void removeReceiver(int frequency, WirelessLinkTile tile);
	
}
