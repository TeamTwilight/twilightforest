package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.WebwormModel;
import twilightforest.client.renderer.block.WebwormRenderer;

public record WebwormSpecialRenderer(WebwormModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		WebwormRenderer.renderWebworm(this.model(), BugModelAnimationHelper.currentYaw, 0.0F, Direction.NORTH, stack, source, light, overlay, null, 1);
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<WebwormSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(WebwormSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<WebwormSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			return new WebwormSpecialRenderer(new WebwormModel(set.bakeLayer(TFModelLayers.WEBWORM)));
		}
	}
}
