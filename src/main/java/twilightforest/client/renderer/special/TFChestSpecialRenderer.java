package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public record TFChestSpecialRenderer(ChestModel model, Material material, float openness) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		VertexConsumer vertexconsumer = this.material.buffer(source, RenderType::entityCutout);
		this.model.setupAnim(this.openness);
		this.model.renderToBuffer(stack, vertexconsumer, light, overlay);
	}

	public record Unbaked(ResourceLocation texture, float openness) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<TFChestSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("texture").forGetter(TFChestSpecialRenderer.Unbaked::texture),
					Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(TFChestSpecialRenderer.Unbaked::openness)
				)
				.apply(instance, TFChestSpecialRenderer.Unbaked::new)
		);

		public Unbaked(ResourceLocation location) {
			this(location, 0.0F);
		}

		@Override
		public MapCodec<TFChestSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			ChestModel chestmodel = new ChestModel(set.bakeLayer(ModelLayers.CHEST));
			Material material = Sheets.chestMaterial(this.texture);
			return new TFChestSpecialRenderer(chestmodel, material, this.openness());
		}
	}
}
