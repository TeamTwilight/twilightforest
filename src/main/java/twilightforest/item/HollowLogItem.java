package twilightforest.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class HollowLogItem extends BlockItem {
	private final Block horizontalLog;
	private final Block verticalLog;
	private final Block climbable;

	public HollowLogItem(Holder<Block> horizontalLog, Holder<Block> verticalLog, Holder<Block> climbable, Properties properties) {
		super(verticalLog.value(), properties);
		this.horizontalLog = horizontalLog.value();
		this.verticalLog = verticalLog.value();
		this.climbable = climbable.value();
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context) {
		return switch (context.getClickedFace().getAxis()) {
			case Y -> this.verticalLog.getStateForPlacement(context);
			case X, Z -> this.horizontalLog.getStateForPlacement(context);
		};
	}

	@Override
	public void registerBlocks(Map<Block, Item> blockItemMap, Item item) {
		super.registerBlocks(blockItemMap, item);
		blockItemMap.put(this.horizontalLog, item);
		blockItemMap.put(this.verticalLog, item);
		blockItemMap.put(this.climbable, item);
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
		return 300;
	}
}