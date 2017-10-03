package twilightforest.item;

import net.minecraft.block.Block;

public class ItemBlockTFLeaves extends ItemBlockTFMeta {
	public ItemBlockTFLeaves(Block block) {
		super(block);
	}

	@Override
	public int getMetadata(int stackMeta) {
		// set meta 4 on placed leaves, which sets the DECAYABLE property to false
		return stackMeta | 4;
	}
}
