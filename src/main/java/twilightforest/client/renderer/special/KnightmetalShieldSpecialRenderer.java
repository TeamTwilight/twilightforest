package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KnightmetalShieldModel;

import javax.annotation.Nullable;
import java.util.Objects;

public record KnightmetalShieldSpecialRenderer(KnightmetalShieldModel model) implements NoDataSpecialModelRenderer {

	private static final Material SHIELD_BASE = new Material(Sheets.SHIELD_SHEET, TwilightForestMod.prefix("entity/knightmetal_shield"));

	@Override
    public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
        stack.pushPose();
        stack.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer vertexconsumer = SHIELD_BASE.sprite().wrap(ItemRenderer.getFoilBuffer(source, this.model().renderType(SHIELD_BASE.atlasLocation()), context == ItemDisplayContext.GUI, foil));
        this.model().renderToBuffer(stack, vertexconsumer, light, overlay);
        stack.popPose();
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<KnightmetalShieldSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(KnightmetalShieldSpecialRenderer.Unbaked::new);

        @Override
        public MapCodec<KnightmetalShieldSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet set) {
            return new KnightmetalShieldSpecialRenderer(new KnightmetalShieldModel(set.bakeLayer(TFModelLayers.KNIGHTMETAL_SHIELD)));
        }
    }
}
