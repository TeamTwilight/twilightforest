package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.LightableBlock;

public class IgniteLightableDispenseBehavior extends OptionalDispenseItemBehavior {

	private final DispenseItemBehavior vanillaBehavior;

	public IgniteLightableDispenseBehavior(DispenseItemBehavior vanillaBehavior) {
		this.vanillaBehavior = vanillaBehavior;
	}

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		if (!level.isClientSide()) {
			BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
			this.setSuccess(tryLightBlock(level, blockpos));
			if (this.isSuccess() && stack.hurt(1, level.getRandom(), null)) {
				stack.setCount(0);
			}
		}

		return this.vanillaBehavior.dispense(source, stack);
	}

	private static boolean tryLightBlock(ServerLevel level, BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.getBlock() instanceof LightableBlock) {
			LightableBlock.Lighting lightValue = blockstate.getValue(LightableBlock.LIGHTING);
			if (lightValue == LightableBlock.Lighting.NONE) {
				level.setBlockAndUpdate(pos, blockstate.setValue(LightableBlock.LIGHTING, LightableBlock.Lighting.NORMAL));
				return true;
			}
		}

		return false;
	}
}
