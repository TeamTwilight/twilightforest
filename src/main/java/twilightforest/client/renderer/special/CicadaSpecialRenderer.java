package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.client.renderer.block.CicadaRenderer;
import twilightforest.client.renderer.block.SkullChestRenderer;

import java.util.function.Function;

public record CicadaSpecialRenderer(CicadaModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		CicadaRenderer.renderCicada(this.model(), BugModelAnimationHelper.currentYaw, 0.0F, Direction.NORTH, stack, source, light, overlay);
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<CicadaSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(CicadaSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<CicadaSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			return new CicadaSpecialRenderer(new CicadaModel(set.bakeLayer(TFModelLayers.CICADA)));
		}
	}
}
