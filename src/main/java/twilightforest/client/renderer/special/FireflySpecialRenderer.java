package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.model.entity.FireflyModel;
import twilightforest.client.renderer.block.CicadaRenderer;
import twilightforest.client.renderer.block.FireflyRenderer;

public record FireflySpecialRenderer(FireflyModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		FireflyRenderer.renderFirefly(this.model(), BugModelAnimationHelper.currentYaw, BugModelAnimationHelper.glowIntensity, 0.0F, Direction.NORTH, stack, source, light, overlay);
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<FireflySpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(FireflySpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<FireflySpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			return new FireflySpecialRenderer(new FireflyModel(set.bakeLayer(TFModelLayers.FIREFLY)));
		}
	}
}
