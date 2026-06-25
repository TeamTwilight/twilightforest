package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
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
import org.joml.Vector3fc;
import tamaized.beanification.Autowired;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.components.item.JarLid;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

import java.util.Optional;
import java.util.function.Consumer;

public record MasonJarSpecialRenderer(Optional<Item> defaultLid, ItemModelResolver resolver) implements SpecialModelRenderer<DataComponentMap> {

	@Autowired(dist = Dist.CLIENT)
	private static TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

	@Override
	public void submit(@Nullable DataComponentMap map, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		if (map != null) {
			stack.pushPose();
			JarLid jarLid = map.get(TFDataComponents.JAR_LID.get());
			Item testLid = jarLid == null ? this.defaultLid().orElse(null) : jarLid.lid();
			Item lid = testLid == null || !JarRenderer.LIDS.containsKey(testLid) ? null : testLid;
			if (lid != null) {
				// JarRenderer was removed in 26.1.2; jar lid model rendering requires NeoForge's new model system
			//JarRenderer.renderModel(JarRenderer.LIDS.get(lid), TFBlocks.MASON_JAR.get().defaultBlockState(), Minecraft.getInstance().getBlockRenderer(), stack, source, light, overlay);
			}

			ItemContainerContents contents = map.get(DataComponents.CONTAINER);
			if (contents != null) {
				stack.pushPose();
				stack.translate(0.5D, 0.4375D, 0.5D);
				stack.scale(0.5F, 0.5F, 0.5F);
				ItemStackRenderState state = new ItemStackRenderState();
				this.resolver().updateForTopItem(state, contents.copyOne(), itemDisplayContextEnumExtension.JARRED, null, null, 0);
				state.submit(stack, collector, light, overlay, outlineColor);
				stack.popPose();
			}
			stack.popPose();
		}
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {

	}

	@Override
	public DataComponentMap extractArgument(ItemStack stack) {
		return stack.getComponents();
	}

	public record Unbaked(Optional<Item> defaultLid) implements SpecialModelRenderer.Unbaked<DataComponentMap> {
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
		public MasonJarSpecialRenderer bake(BakingContext context) {
			return new MasonJarSpecialRenderer(this.defaultLid(), Minecraft.getInstance().getItemModelResolver());
		}
	}
}
