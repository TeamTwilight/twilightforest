package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class AbstractParticleSpawnerBlock extends Block {

	public static final IntegerProperty RADIUS = IntegerProperty.create("particle_radius", 1, 10);

	public AbstractParticleSpawnerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(RADIUS, 1));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int radius = state.getValue(RADIUS);
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for (int partCount = 0; partCount < getParticleCountPerSpawn(state); ++partCount) {
			mutablePos.set(x + Mth.nextInt(random, -radius, radius), y + Mth.nextInt(random, -radius, radius), z + Mth.nextInt(random, -radius, radius));
			BlockState offState = level.getBlockState(mutablePos);
			if (!offState.isCollisionShapeFullBlock(level, mutablePos)) {
				level.addParticle((ParticleOptions) getParticlesToSpawn(), (double) mutablePos.getX() + random.nextDouble(), (double) mutablePos.getY() + random.nextDouble(), (double) mutablePos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
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
