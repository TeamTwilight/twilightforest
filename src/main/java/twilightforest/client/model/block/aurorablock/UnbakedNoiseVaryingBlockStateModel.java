package twilightforest.client.model.block.aurorablock;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

import java.util.List;

public record UnbakedNoiseVaryingBlockStateModel(List<Identifier> models) implements CustomUnbakedBlockStateModel {
	public static final MapCodec<UnbakedNoiseVaryingBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Identifier.CODEC.listOf(1, Integer.MAX_VALUE).fieldOf("models").forGetter(UnbakedNoiseVaryingBlockStateModel::models))
		.apply(instance, UnbakedNoiseVaryingBlockStateModel::new));

	@Override
	public BlockStateModel bake(ModelBaker modelBaker) {
		BlockStateModel[] bakedBlockModels = new BlockStateModel[this.models.size()];

		for (int index = 0; index < bakedBlockModels.length; index++) {
			bakedBlockModels[index] = new SingleVariant(SimpleModelWrapper.bake(modelBaker, this.models.get(index), BlockModelRotation.IDENTITY));
		}

		return new NoiseVaryingModel(bakedBlockModels);
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
		for (Identifier model : this.models) {
			resolver.markDependency(model);
		}
	}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MAP_CODEC;
	}
}
