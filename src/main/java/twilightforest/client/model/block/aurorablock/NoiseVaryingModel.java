package twilightforest.client.model.block.aurorablock;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import twilightforest.util.SimplexNoiseHelper;

import java.util.List;

public class NoiseVaryingModel implements BlockStateModel, DynamicBlockStateModel {
	private final BlockStateModel[] variants;

	public NoiseVaryingModel(BlockStateModel[] variants) {
		this.variants = variants;
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
		this.variants[0].collectParts(random, parts);
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.variants[0].particleMaterial();
	}

	@Override
	public @MaterialFlags int materialFlags() {
		return this.variants[0].materialFlags();
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
