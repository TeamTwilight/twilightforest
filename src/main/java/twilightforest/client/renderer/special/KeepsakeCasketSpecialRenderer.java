package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.client.renderer.block.KeepsakeCasketRenderer;
import twilightforest.init.TFDataComponents;

public record KeepsakeCasketSpecialRenderer(KeepsakeCasketModel model, float openness) implements SpecialModelRenderer<Integer> {

	@Override
	public Integer extractArgument(ItemStack stack) {
		return stack.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0);
	}

	@Override
	public void render(Integer damage, ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		KeepsakeCasketRenderer.renderCasket(this.openness, stack, source, light, overlay, KeepsakeCasketRenderer.getTextureLocation(damage), Direction.NORTH, this.model);
	}

	public record Unbaked(float openness) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<KeepsakeCasketSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(KeepsakeCasketSpecialRenderer.Unbaked::openness))
			.apply(instance, KeepsakeCasketSpecialRenderer.Unbaked::new));

		public Unbaked() {
			this(0.0F);
		}

		@Override
		public MapCodec<KeepsakeCasketSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			KeepsakeCasketModel model = new KeepsakeCasketModel(set.bakeLayer(TFModelLayers.KEEPSAKE_CASKET));
			return new KeepsakeCasketSpecialRenderer(model, this.openness);
		}
	}
}
