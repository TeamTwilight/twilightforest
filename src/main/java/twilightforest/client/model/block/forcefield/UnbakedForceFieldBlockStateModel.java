package twilightforest.client.model.block.forcefield;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

public record UnbakedForceFieldBlockStateModel(Identifier modelId) implements CustomUnbakedBlockStateModel {

	public static final MapCodec<UnbakedForceFieldBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Identifier.CODEC.fieldOf("model").forGetter(UnbakedForceFieldBlockStateModel::modelId)
	).apply(instance, UnbakedForceFieldBlockStateModel::new));

	@Override
	public BlockStateModel bake(ModelBaker modelBaker) {
		ResolvedModel resolved = modelBaker.getModel(this.modelId);
		TextureSlots textureSlots = resolved.getTopTextureSlots();
		ContextMap additionalProperties = resolved.getTopAdditionalProperties();
		UnbakedModel wrapped = resolved.wrapped();

		if (wrapped instanceof UnbakedForceFieldModel forceField) {
			return forceField.bakeInternal(
				textureSlots,
				modelBaker,
				BlockModelRotation.IDENTITY,
				true,
				true,
				null,
				additionalProperties
			);
		}

		return new SingleVariant(SimpleModelWrapper.bake(modelBaker, this.modelId, BlockModelRotation.IDENTITY));
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
		resolver.markDependency(this.modelId);
	}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MAP_CODEC;
	}
}
