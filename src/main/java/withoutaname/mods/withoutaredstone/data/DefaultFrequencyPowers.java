package withoutaname.mods.withoutaredstone.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.util.math.BlockPos;

import withoutaname.mods.withoutaredstone.blocks.WirelessLinkTile;

public class DefaultFrequencyPowers implements IFrequencyPowers {
	
	private final HashMap<Long, List<WirelessLinkTile>> receivers = new HashMap<>();
	private HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers = new HashMap<>();
	
	@Override
	public HashMap<Long, HashMap<BlockPos, Integer>> getFrequencyPowers() {
		return frequencyPowers;
	}
	
	@Override
	public void setFrequencyPowers(HashMap<Long, HashMap<BlockPos, Integer>> frequencyPowers) {
		this.frequencyPowers = frequencyPowers;
	}
	
	@Override
	public int getPower(long frequency) {
		int power = 0;
		if (frequencyPowers.containsKey(frequency)) {
			for (int value : frequencyPowers.get(frequency).values()) {
				if (power < value) {
					power = value;
				}
			}
		}
		return power;
	}
	
	@Override
	public void removePower(long frequency, BlockPos pos) {
		if (frequencyPowers.containsKey(frequency)) {
			frequencyPowers.get(frequency).remove(pos);
		}
	}
	
	@Override
	public void setPower(long frequency, BlockPos pos, int power) {
		int oldPower = getPower(frequency);
		if (!frequencyPowers.containsKey(frequency)) {
			frequencyPowers.put(frequency, new HashMap<>());
		}
		frequencyPowers.get(frequency).put(pos, power);
		if (oldPower != power && receivers.containsKey(frequency)) {
			notifyReceivers(frequency);
		}
	}
	
	@Override
	public void notifyReceivers(long frequency) {
		if (receivers.containsKey(frequency)) {
			for (WirelessLinkTile tile : receivers.get(frequency)) {
				tile.updateReceiver();
			}
		}
	}
	
	@Override
	public void addReceiver(long frequency, WirelessLinkTile tile) {
		if (!receivers.containsKey(frequency)) {
			receivers.put(frequency, new ArrayList<>());
		}
		List<WirelessLinkTile> wirelessLinkTiles = receivers.get(frequency);
		if (!wirelessLinkTiles.contains(tile)) {
			wirelessLinkTiles.add(tile);
		}
	}
	
	@Override
	public void removeReceiver(long frequency, WirelessLinkTile tile) {
		if (receivers.containsKey(frequency)) {
			receivers.get(frequency).remove(tile);
		}
	}
	
}
