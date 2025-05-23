package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.api.distmarker.Dist;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.components.item.JarLid;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

import java.util.Optional;

public record MasonJarSpecialRenderer(Optional<Item> defaultLid) implements SpecialModelRenderer<DataComponentMap> {

	@Autowired(dist = Dist.CLIENT)
	private static TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

	@Override
	public void render(@Nullable DataComponentMap map, ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		if (map != null) {
			stack.pushPose();
			JarLid jarLid = map.get(TFDataComponents.JAR_LID.get());
			Item testLid = jarLid == null ? this.defaultLid().orElse(null) : jarLid.lid();
			Item lid = testLid == null || !JarRenderer.LIDS.containsKey(testLid) ? null : testLid;
			if (lid != null) {
				JarRenderer.renderModel(JarRenderer.LIDS.get(lid), TFBlocks.MASON_JAR.get().defaultBlockState(), Minecraft.getInstance().getBlockRenderer(), stack, source, light, overlay);
			}

			ItemContainerContents contents = map.get(DataComponents.CONTAINER);
			if (contents != null) {
				stack.pushPose();
				stack.translate(0.5D, 0.4375D, 0.5D);
				stack.scale(0.5F, 0.5F, 0.5F);
				Minecraft.getInstance().getItemRenderer().renderStatic(contents.copyOne(), itemDisplayContextEnumExtension.JARRED, light, overlay, stack, source, null, 0);
				stack.popPose();
			}
			stack.popPose();
		}
	}

	@Override
	public DataComponentMap extractArgument(ItemStack stack) {
		return stack.getComponents();
	}

	public record Unbaked(Optional<Item> defaultLid) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<MasonJarSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("default_lid").forGetter(MasonJarSpecialRenderer.Unbaked::defaultLid))
			.apply(instance, MasonJarSpecialRenderer.Unbaked::new));

		public Unbaked(Item item) {
			this(Optional.of(item));
		}

		public Unbaked() {
			this(Optional.empty());
		}

		@Override
		public MapCodec<MasonJarSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet set) {
			return new MasonJarSpecialRenderer(this.defaultLid());
		}
	}
}
