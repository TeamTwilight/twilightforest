package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.BrazierBlock;
import twilightforest.init.TFBlockEntities;

public class BrazierBlockEntity extends BlockEntity {

	public BrazierBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.BRAZIER.get(), pos, blockState);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBlockEntity entity) {
		if (level.isClientSide()) {
			if (state.getValue(BrazierBlock.LIGHT).isLit()) {
				BlockPos above = pos.above();
				level.addParticle(ParticleTypes.SMOKE, above.getX() + level.random.nextFloat() * 0.4F + 0.3F, above.getY() + 0.9F, above.getZ() + level.random.nextFloat() * 0.4F + 0.3F,
					0.0D, 0.05D, 0.0D);
			}
		}
	}
}
