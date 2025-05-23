package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.AABB;
import twilightforest.item.TransformPowderItem;

public class TransformationDispenseBehavior extends DefaultDispenseItemBehavior {

	boolean fired = false;

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.level();
		BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		if (!level.isClientSide()) {
			for (LivingEntity livingentity : level.getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS)) {
				if (TransformPowderItem.transformEntityIfPossible(livingentity, stack, true)) {
					this.fired = true;
				}
			}
		}
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		if (this.fired) {
			super.playSound(source);
		} else {
			source.level().levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, source.pos(), 0);
		}
	}
}
