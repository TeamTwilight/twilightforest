package twilightforest.client.model.block.aurorablock;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import twilightforest.util.SimplexNoiseHelper;

import java.util.List;

public class NoiseVaryingModel extends DelegateBlockStateModel implements DynamicBlockStateModel {
	private final BlockStateModel[] variants;

	public NoiseVaryingModel(BlockStateModel[] variants) {
		// First variation will propagate properties among other variants
		super(variants[0]);
		this.variants = variants;
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		this.chooseVariant(pos).collectParts(level, pos, state, random, parts);
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.chooseVariant(pos).particleMaterial(level, pos, state);
	}

	@Override
	public @MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.chooseVariant(pos).materialFlags(level, pos, state);
	}

	private BlockStateModel chooseVariant(BlockPos pos) {
		return this.variants[SimplexNoiseHelper.calcVariant(pos, this.variants.length)];
	}

}
