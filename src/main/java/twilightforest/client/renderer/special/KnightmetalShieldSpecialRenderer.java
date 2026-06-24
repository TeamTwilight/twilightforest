package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KnightmetalShieldModel;

import java.util.function.Consumer;

public record KnightmetalShieldSpecialRenderer(SpriteGetter sprites, KnightmetalShieldModel model) implements NoDataSpecialModelRenderer {

	private static final SpriteId SHIELD_BASE = Sheets.SHIELD_MAPPER.apply(TwilightForestMod.prefix("knightmetal_shield"));

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		collector.submitModel(this.model, Unit.INSTANCE, stack, light, overlay, -1, SHIELD_BASE, this.sprites, outlineColor, null);

		if (hasFoil) {
			collector.submitModel(this.model, Unit.INSTANCE, stack, RenderTypes.entityGlint(), light, overlay, -1, this.sprites.get(SHIELD_BASE), 0, null);
		}
    }

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		this.model.root().getExtentsForGui(poseStack, output);
	}

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final KnightmetalShieldSpecialRenderer.Unbaked INSTANCE = new KnightmetalShieldSpecialRenderer.Unbaked();
		public static final MapCodec<KnightmetalShieldSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public MapCodec<KnightmetalShieldSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		public KnightmetalShieldSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
			return new KnightmetalShieldSpecialRenderer(context.sprites(), new KnightmetalShieldModel(context.entityModelSet().bakeLayer(TFModelLayers.KNIGHTMETAL_SHIELD)));
		}
    }
}
