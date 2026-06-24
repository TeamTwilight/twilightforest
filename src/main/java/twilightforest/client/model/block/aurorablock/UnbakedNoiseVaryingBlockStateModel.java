package twilightforest.client.model.block.aurorablock;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

import java.util.List;

public record UnbakedNoiseVaryingBlockStateModel(List<BlockStateModel.Unbaked> models) implements CustomUnbakedBlockStateModel {
	public static final MapCodec<UnbakedNoiseVaryingBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BlockStateModel.Unbaked.CODEC.listOf(1, Integer.MAX_VALUE).fieldOf("models").forGetter(UnbakedNoiseVaryingBlockStateModel::models))
		.apply(instance, UnbakedNoiseVaryingBlockStateModel::new));

	@Override
	public BlockStateModel bake(ModelBaker modelBaker) {
		BlockStateModel[] bakedBlockModels = new BlockStateModel[this.models.size()];

		for (int index = 0; index < bakedBlockModels.length; index++) {
			bakedBlockModels[index] = this.models.get(index).bake(modelBaker);
		}

		return new NoiseVaryingModel(bakedBlockModels);
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
		for (BlockStateModel.Unbaked model : this.models) {
			model.resolveDependencies(resolver);
		}
	}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MAP_CODEC;
	}
}
