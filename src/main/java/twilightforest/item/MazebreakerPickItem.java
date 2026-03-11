package twilightforest.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.data.tags.BlockTagGenerator;

import javax.annotation.Nonnull;

public class MazebreakerPickItem extends PickaxeItem {
	public MazebreakerPickItem(Tier material, Properties properties) {
		super(material, properties);
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		return state.is(BlockTagGenerator.MAZEBREAKER_ACCELERATED) ? destroySpeed * 16.0F : destroySpeed;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}
}