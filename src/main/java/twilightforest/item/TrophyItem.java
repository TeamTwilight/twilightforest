package twilightforest.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;

public class TrophyItem extends StandingAndWallBlockItem {

	public TrophyItem(Block floorBlock, Block wallBlock, Properties properties) {
		super(floorBlock, wallBlock, Direction.DOWN, properties);
	}
}