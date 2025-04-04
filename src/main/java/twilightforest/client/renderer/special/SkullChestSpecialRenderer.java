package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.client.renderer.block.KeepsakeCasketRenderer;
import twilightforest.client.renderer.block.SkullChestRenderer;
import twilightforest.init.TFDataComponents;

public record SkullChestSpecialRenderer(KeepsakeCasketModel model, float openness) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		SkullChestRenderer.renderCasket(this.openness(), stack, source, light, overlay, SkullChestRenderer.SKULL_CHEST_TEXTURE, Direction.NORTH, this.model());
	}

	public record Unbaked(float openness) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<SkullChestSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(SkullChestSpecialRenderer.Unbaked::openness))
			.apply(instance, SkullChestSpecialRenderer.Unbaked::new));

		public Unbaked() {
			this(0.0F);
		}

		@Override
		public MapCodec<SkullChestSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			KeepsakeCasketModel model = new KeepsakeCasketModel(set.bakeLayer(TFModelLayers.SKULL_CHEST));
			return new SkullChestSpecialRenderer(model, this.openness());
		}
	}
}
