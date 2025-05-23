package twilightforest.client.model.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.client.renderer.special.FullBrightSpecialRenderer;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FullbrightItemModel implements ItemModel {
	private final Unbaked unbaked;
	private final BakedModel bakedModel;

	public FullbrightItemModel(Unbaked unbaked, BakedModel bakedModel) {
		this.unbaked = unbaked;
		this.bakedModel = bakedModel;
	}

	@Override
	public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext context, @Nullable ClientLevel level, @Nullable LivingEntity living, int seed) {
		final int[] tints = new int[unbaked.tints.size()];
		for (int j = 0; j < tints.length; j++) tints[j] = unbaked.tints.get(j).calculate(stack, level, living);

		ItemStackRenderState.LayerRenderState renderState = state.newLayer();
		if (stack.hasFoil()) renderState.setFoilType(ItemStackRenderState.FoilType.STANDARD);

		int[] prepared = renderState.prepareTintLayers(tints.length);
		System.arraycopy(tints, 0, prepared, 0, tints.length);

		renderState.setupSpecialModel(new FullBrightSpecialRenderer(this.bakedModel.getRenderType(stack), this.bakedModel, prepared, this.unbaked.fullbrightFaces), null, this.bakedModel);
	}

	@OnlyIn(Dist.CLIENT)
	public record Unbaked(ResourceLocation model, List<ItemTintSource> tints, List<Integer> fullbrightFaces) implements ItemModel.Unbaked {
		public static final MapCodec<FullbrightItemModel.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("model").forGetter(FullbrightItemModel.Unbaked::model),
				ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(FullbrightItemModel.Unbaked::tints),
				Codec.INT.listOf().fieldOf("fullbright_faces").forGetter(FullbrightItemModel.Unbaked::fullbrightFaces)
			).apply(instance, FullbrightItemModel.Unbaked::new)
		);

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			resolver.resolve(this.model);
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context) {
			return new FullbrightItemModel(this, context.bake(this.model));
		}

		@Override
		public MapCodec<FullbrightItemModel.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
