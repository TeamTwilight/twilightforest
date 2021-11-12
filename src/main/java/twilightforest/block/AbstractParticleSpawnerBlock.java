package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Random;

public abstract class AbstractParticleSpawnerBlock extends Block {

	public static final IntegerProperty RADIUS = IntegerProperty.create("particle_radius", 1, 10);

	public AbstractParticleSpawnerBlock(Properties p_49795_) {
		super(p_49795_);
		registerDefaultState(getStateDefinition().any().setValue(RADIUS, 1));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = state.getValue(RADIUS);
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for(int partCount = 0; partCount < getParticleCountPerSpawn(state); ++partCount) {
			mutablePos.set(x + Mth.nextInt(rand, -radius, radius), y + Mth.nextInt(rand, -radius, radius), z + Mth.nextInt(rand, -radius, radius));
			BlockState var16 = world.getBlockState(mutablePos);
			if (!var16.isCollisionShapeFullBlock(world, mutablePos)) {
				world.addParticle((ParticleOptions) getParticlesToSpawn(), (double)mutablePos.getX() + rand.nextDouble(), (double)mutablePos.getY() + rand.nextDouble(), (double)mutablePos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public abstract ParticleType<?> getParticlesToSpawn();

	public abstract int getParticleCountPerSpawn(BlockState state);

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(RADIUS);
	}
}
