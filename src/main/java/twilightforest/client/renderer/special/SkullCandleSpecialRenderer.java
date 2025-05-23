package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.components.item.SkullCandles;
import twilightforest.init.TFDataComponents;

import java.util.Optional;

public record SkullCandleSpecialRenderer(SkullBlock.Type skullType, SkullModelBase model, @Nullable ResourceLocation textureOverride, float animation) implements SpecialModelRenderer<Pair<ResolvableProfile, SkullCandles>> {

	@NotNull
	@Override
	public Pair<ResolvableProfile, SkullCandles> extractArgument(ItemStack stack) {
		return Pair.of(stack.get(DataComponents.PROFILE), stack.get(TFDataComponents.SKULL_CANDLES));
	}

	@Override
	public void render(@NotNull Pair<ResolvableProfile, SkullCandles> candles, ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		RenderType rendertype = SkullBlockRenderer.getRenderType(this.skullType(), candles.getFirst(), this.textureOverride());
		SkullBlockRenderer.renderSkull(null, 180.0F, this.animation(), stack, source, light, this.model(), rendertype);

		SkullCandles skullCandles = candles.getSecond();

		if (skullCandles != null) {
			stack.translate(0.0F, 0.5F, 0.0F);
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
				AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(skullCandles.color()))
					.defaultBlockState().setValue(CandleBlock.CANDLES, skullCandles.count()), stack, source, light, overlay);
		}
	}

	public record Unbaked(SkullBlock.Type kind, Optional<ResourceLocation> textureOverride, float animation) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<SkullCandleSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				SkullBlock.Type.CODEC.fieldOf("kind").forGetter(SkullCandleSpecialRenderer.Unbaked::kind),
				ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(SkullCandleSpecialRenderer.Unbaked::textureOverride),
				Codec.FLOAT.optionalFieldOf("animation", 0.0F).forGetter(SkullCandleSpecialRenderer.Unbaked::animation))
			.apply(instance, SkullCandleSpecialRenderer.Unbaked::new));

		public Unbaked(SkullBlock.Type kind) {
			this(kind, Optional.empty(), 0.0F);
		}

		public MapCodec<SkullCandleSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Nullable
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			SkullModelBase skullmodelbase = SkullBlockRenderer.createModel(set, this.kind());
			ResourceLocation resourcelocation = this.textureOverride().map(location -> location.withPath(path -> "textures/entity/" + path + ".png")).orElse(null);
			return skullmodelbase != null ? new SkullCandleSpecialRenderer(this.kind(), skullmodelbase, resourcelocation, this.animation()) : null;
		}
	}
}
