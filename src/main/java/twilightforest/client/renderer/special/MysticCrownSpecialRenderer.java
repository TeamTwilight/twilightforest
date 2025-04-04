package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.LichModel;
import twilightforest.client.renderer.entity.LichRenderer;

public record MysticCrownSpecialRenderer(LichModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		stack.pushPose();
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.scale(1.0F, -1.0F, -1.0F);
		VertexConsumer vertexconsumer = source.getBuffer(this.model.renderType(LichRenderer.TEXTURE));
		this.model().hat.render(stack, vertexconsumer, light, overlay);
		stack.popPose();
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<MysticCrownSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(MysticCrownSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<MysticCrownSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			return new MysticCrownSpecialRenderer(new LichModel(set.bakeLayer(TFModelLayers.LICH_TROPHY)));
		}
	}
}
