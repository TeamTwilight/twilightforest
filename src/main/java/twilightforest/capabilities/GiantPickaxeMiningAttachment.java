package twilightforest.capabilities;

import net.minecraft.nbt.CompoundTag;

public class GiantPickaxeMiningAttachment {

	private long mining;
	private boolean breaking;
	private int giantBlockConversion;

	public void setMining(long mining) {
		this.mining = mining;
	}

	public long getMining() { // Is block breaking with the use of a giant pickaxe occurring
		return this.mining;
	}

	public void setBreaking(boolean breaking) {
		this.breaking = breaking;
	}

	public boolean getBreaking() { // Is code in the process of breaking a 4x4x4 cube of blocks
		return this.breaking;
	}

	public void setGiantBlockConversion(int giantBlockConversion) {
		this.giantBlockConversion = giantBlockConversion;
	}

	public int getGiantBlockConversion() {
		return this.giantBlockConversion;
	}

	public boolean canMakeGiantBlock() { // Is code in the process of converting block drops to a giant block drop
		return this.giantBlockConversion > 0;
	}
}
