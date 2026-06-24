package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFDataMaps;

public class CrumbleDispenseBehavior extends DefaultDispenseItemBehavior {

	boolean fired = false;

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		BlockState state = level.getBlockState(pos);
		if (!(stack.getMaxDamage() == stack.getDamageValue() + 1)) {
			var resultBlock = state.getBlock().builtInRegistryHolder().getData(TFDataMaps.CRUMBLE_HORN);
			if (resultBlock != null) {
				if (resultBlock.result() == Blocks.AIR) {
					level.destroyBlock(pos, true);
				} else {
					level.setBlock(pos, resultBlock.result().withPropertiesOf(state), Block.UPDATE_ALL);
					level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				}

				stack.hurtAndBreak(1, level, null, item -> {});
				this.fired = true;
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
			source.level().levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, source.pos(), 0);
		}
	}

}
