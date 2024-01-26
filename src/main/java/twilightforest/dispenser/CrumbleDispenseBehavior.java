package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFRecipes;

public class CrumbleDispenseBehavior extends DefaultDispenseItemBehavior {

	boolean fired = false;

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.level();
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		BlockState state = level.getBlockState(pos);
		if (!level.isClientSide()) {
			if (!(stack.getMaxDamage() == stack.getDamageValue() + 1)) {
				level.getRecipeManager().getAllRecipesFor(TFRecipes.CRUMBLE_RECIPE.get()).forEach(recipeHolder -> {
					if (recipeHolder.value().input() == state.getBlock()) {
						if (recipeHolder.value().result() == Blocks.AIR) {
							level.removeBlock(pos, true);
							level.levelEvent(2001, pos, Block.getId(state));
						} else {
							level.setBlock(pos, recipeHolder.value().result().withPropertiesOf(state), 3);
						}
						stack.hurt(1, level.getRandom(), null);
						this.fired = true;
					}
				});
			}
		}
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		if (this.fired) {
			super.playSound(source);
			this.fired = false;
		} else {
			source.level().levelEvent(1001, source.pos(), 0);
		}
	}

}
