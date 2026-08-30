package twilightforest.world.components;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public class NoiseCarverWallProvider extends NoiseProvider implements CarverWallProvider {

	public NoiseCarverWallProvider(long seed, NormalNoise.NoiseParameters parameters, float scale, List<BlockState> states) {
		super(seed, parameters, scale, states);
	}

	@Override
	public BlockState getState(RandomSource random, BlockPos pos) {
		return this.getRandomState(this.states, pos, this.scale);
	}
}
