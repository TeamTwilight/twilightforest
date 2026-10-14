package twilightforest.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import twilightforest.action.PeacockFeatherFanAction;

@Configurable
public class FeatherFanDispenseBehavior extends DefaultDispenseItemBehavior {

	@Autowired
	private PeacockFeatherFanAction peacockFeatherFanAction;

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		int fanned = peacockFeatherFanAction.fan(level, source.center(), source.state().getValue(DispenserBlock.FACING).getUnitVec3(), stack);
		stack.hurtAndBreak(fanned + 1, level, null, _ -> {});
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		peacockFeatherFanAction.playWhoosh(source.level(), source.pos(), SoundSource.BLOCKS);
	}

	//Particle woooosh
	@Override
	protected void playAnimation(BlockSource source, Direction direction) {
		peacockFeatherFanAction.spawnParticles(source.level(), source.center(), direction.getUnitVec3());
	}
}
