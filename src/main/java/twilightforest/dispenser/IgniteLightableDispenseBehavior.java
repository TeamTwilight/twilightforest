package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.AbstractLightableBlock;

public class IgniteLightableDispenseBehavior extends OptionalDispenseItemBehavior {

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.getLevel();
		if (!level.isClientSide()) {
			BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
			this.setSuccess(tryLightBlock(level, blockpos));
			if (this.isSuccess() && stack.hurt(1, level.getRandom(), null)) {
				stack.setCount(0);
			}
		}

		return stack;
	}

	private static boolean tryLightBlock(ServerLevel level, BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.getBlock() instanceof AbstractLightableBlock) {
			AbstractLightableBlock.Lighting lightValue = blockstate.getValue(AbstractLightableBlock.LIGHTING);
			if (lightValue == AbstractLightableBlock.Lighting.NONE) {
				level.setBlockAndUpdate(pos, blockstate.setValue(AbstractLightableBlock.LIGHTING, AbstractLightableBlock.Lighting.NORMAL));
				return true;
			}
		}

		return false;
	}
}
