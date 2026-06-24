package twilightforest.client.model.block.giantblock;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import twilightforest.block.GiantBlock;

import java.util.List;

public class GiantBlockModel implements BlockStateModel, DynamicBlockStateModel {

	private final BlockStateModel[] voxels;

	public GiantBlockModel(BlockStateModel[] voxels) {
		this.voxels = voxels;
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		this.voxels[GiantBlock.packCoords(pos)].collectParts(level, pos, state, random, parts);
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.voxels[GiantBlock.packCoords(pos)].particleMaterial(level, pos, state);
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.voxels[0].particleMaterial();
	}

	@Override
	public @MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.voxels[GiantBlock.packCoords(pos)].materialFlags(level, pos, state);
	}

	@Override
	public @MaterialFlags int materialFlags() {
		return this.voxels[0].materialFlags();
	}
}
