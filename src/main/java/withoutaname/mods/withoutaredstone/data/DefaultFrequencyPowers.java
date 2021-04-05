package withoutaname.mods.withoutaredstone.data;

import net.minecraft.util.math.BlockPos;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultFrequencyPowers implements IFrequencyPowers {

	private HashMap<Integer, HashMap<BlockPos, Integer>> frequencyPowers = new HashMap<>();

	private final HashMap<Integer, List<WirelessLinkTile>> receivers = new HashMap<>();

	@Override
	public HashMap<Integer, HashMap<BlockPos, Integer>> getFrequencyPowers() {
		return frequencyPowers;
	}

	@Override
	public void setFrequencyPowers(HashMap<Integer, HashMap<BlockPos, Integer>> frequencyPowers) {
		this.frequencyPowers = frequencyPowers;
	}

	@Override
	public int getPower(int frequency) {
		int power = 0;
		if (frequencyPowers.containsKey(frequency)){
			for (int value : frequencyPowers.get(frequency).values()) {
				if (power < value) {
					power = value;
				}
			}
		}
		return power;
	}

	@Override
	public void removePower(int frequency, BlockPos pos) {
		if (frequencyPowers.containsKey(frequency)) {
			frequencyPowers.get(frequency).remove(pos);
		}
	}

	@Override
	public void setPower(int frequency, BlockPos pos, int power) {
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
	public void notifyReceivers(int frequency) {
		if (receivers.containsKey(frequency)) {
			for (WirelessLinkTile tile : receivers.get(frequency)) {
				tile.updateReceiver();
			}
		}
	}

	@Override
	public void addReceiver(int frequency, WirelessLinkTile tile) {
		if (!receivers.containsKey(frequency)) {
			receivers.put(frequency, new ArrayList<>());
		}
		List<WirelessLinkTile> wirelessLinkTiles = receivers.get(frequency);
		if (!wirelessLinkTiles.contains(tile)) {
			wirelessLinkTiles.add(tile);
		}
	}

	@Override
	public void removeReceiver(int frequency, WirelessLinkTile tile) {
		if (receivers.containsKey(frequency)) {
			receivers.get(frequency).remove(tile);
		}
	}
}
