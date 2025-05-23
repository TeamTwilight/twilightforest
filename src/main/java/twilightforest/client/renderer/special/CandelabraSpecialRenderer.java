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
import net.minecraft.core.BlockPos;
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
import twilightforest.block.entity.CandelabraBlockEntity;
import twilightforest.client.renderer.block.CandelabraRenderer;
import twilightforest.components.item.CandelabraData;
import twilightforest.components.item.SkullCandles;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

import java.util.Optional;

public record CandelabraSpecialRenderer() implements SpecialModelRenderer<CandelabraData> {

	@Nullable
	@Override
	public CandelabraData extractArgument(ItemStack stack) {
		return stack.get(TFDataComponents.CANDELABRA_DATA);
	}

	@Override
	public void render(@Nullable CandelabraData data, ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		if (data != null) {
			CandelabraRenderer.renderCandles(TFBlocks.CANDELABRA.get().defaultBlockState(), data, stack, source, light, overlay);
		}
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<CandelabraSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(CandelabraSpecialRenderer.Unbaked::new);

		public MapCodec<CandelabraSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			return new CandelabraSpecialRenderer();
		}
	}
}
