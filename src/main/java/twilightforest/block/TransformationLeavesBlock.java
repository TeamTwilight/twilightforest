package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFParticleType;

public class TransformationLeavesBlock extends LeavesBlock {

	public static final MapCodec<TransformationLeavesBlock> CODEC = simpleCodec(TransformationLeavesBlock::new);

	public TransformationLeavesBlock(BlockBehaviour.Properties properties) {
		super(1.0F, properties);
	}

	@Override
	public MapCodec<? extends LeavesBlock> codec() {
		return CODEC;
	}

	@Override
	protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
		ParticleUtils.spawnParticlesOnBlockFace(level, pos, TFParticleType.LEAF_RUNE.get(), ConstantInt.of(1), Direction.DOWN, () -> Vec3.ZERO, 0.5F);
	}
}
