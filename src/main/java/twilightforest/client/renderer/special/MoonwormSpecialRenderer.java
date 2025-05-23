package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.client.renderer.block.CicadaRenderer;
import twilightforest.client.renderer.block.MoonwormRenderer;

public record MoonwormSpecialRenderer(MoonwormModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		MoonwormRenderer.renderMoonworm(this.model(), BugModelAnimationHelper.currentRotation, 0.0F, (BugModelAnimationHelper.desiredRotation - BugModelAnimationHelper.currentRotation) - Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks(), BugModelAnimationHelper.yawWriggleDelay, Direction.NORTH, stack, source, light, overlay);
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<MoonwormSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(MoonwormSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<MoonwormSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			return new MoonwormSpecialRenderer(new MoonwormModel(set.bakeLayer(TFModelLayers.MOONWORM)));
		}
	}
}
