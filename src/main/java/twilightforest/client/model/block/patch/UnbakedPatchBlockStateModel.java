package twilightforest.client.model.block.patch;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

public record UnbakedPatchBlockStateModel(Identifier modelId) implements CustomUnbakedBlockStateModel {

	public static final MapCodec<UnbakedPatchBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Identifier.CODEC.fieldOf("model").forGetter(UnbakedPatchBlockStateModel::modelId)
	).apply(instance, UnbakedPatchBlockStateModel::new));

	@Override
	public BlockStateModel bake(ModelBaker baker) {
		ResolvedModel resolved = baker.getModel(this.modelId);
		if (resolved.wrapped() instanceof UnbakedPatchModel patch) {
			TextureSlots textureSlots = resolved.getTopTextureSlots();
			return patch.bakeInternal(textureSlots, baker, BlockModelRotation.IDENTITY, true, true, null, resolved.getTopAdditionalProperties());
		}
		return new SingleVariant(SimpleModelWrapper.bake(baker, this.modelId, BlockModelRotation.IDENTITY));
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