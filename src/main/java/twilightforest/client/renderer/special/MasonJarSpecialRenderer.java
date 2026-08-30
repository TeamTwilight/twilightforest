package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.api.distmarker.Dist;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import tamaized.beanification.Autowired;
import twilightforest.client.renderer.block.jar.JarLidResolver;
import twilightforest.client.renderer.block.jar.TexturedJarLidPart;
import twilightforest.components.item.JarLid;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFDataComponents;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record MasonJarSpecialRenderer(Optional<Item> defaultLid, ItemModelResolver itemModelResolver) implements SpecialModelRenderer<DataComponentMap> {

	@Autowired(dist = Dist.CLIENT)
	private static TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

	@Autowired(dist = Dist.CLIENT)
	private static JarLidResolver jarLidResolver;

	@Override
	public void submit(@Nullable DataComponentMap components, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		if (components == null) {
			return;
		}

		poseStack.pushPose();

		renderLid(components, poseStack, collector, light, overlay, outlineColor);
		renderContents(components, poseStack, collector, light, overlay, outlineColor);

		poseStack.popPose();
	}

	private void renderLid(DataComponentMap components, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, int outlineColor) {
		JarLid jarLid = components.get(TFDataComponents.JAR_LID.get());
		Item lid = jarLid != null ? jarLid.lid() : defaultLid.orElse(null);

		if (lid == null) {
			return;
		}

		TexturedJarLidPart lidPart = jarLidResolver.resolve(lid);

		if (lidPart == null) {
			return;
		}

		collector.submitMultiLayerBlockModel(
			poseStack,
			List.of(lidPart),
			false,
			new int[0],
			light,
			overlay,
			outlineColor
		);
	}

	private void renderContents(DataComponentMap components, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, int outlineColor) {
		ItemContainerContents contents = components.get(DataComponents.CONTAINER);
		if (contents == null) {
			return;
		}

		ItemStack item = contents.copyOne();
		if (item.isEmpty()) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0.5D, 0.4375D, 0.5D);
		poseStack.scale(0.5F, 0.5F, 0.5F);

		ItemStackRenderState renderState = new ItemStackRenderState();
		itemModelResolver.updateForTopItem(
			renderState,
			item,
			itemDisplayContextEnumExtension.JARRED,
			null,
			null,
			0
		);

		renderState.submit(
			poseStack,
			collector,
			light,
			overlay,
			outlineColor
		);

		poseStack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
	}

	@Override
	public DataComponentMap extractArgument(ItemStack stack) {
		return stack.getComponents();
	}

	public record Unbaked(Optional<Item> defaultLid) implements SpecialModelRenderer.Unbaked<DataComponentMap> {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.ITEM.byNameCodec()
				.optionalFieldOf("default_lid")
				.forGetter(Unbaked::defaultLid))
			.apply(instance, Unbaked::new));

		public Unbaked(Item item) {
			this(Optional.of(item));
		}

		public Unbaked() {
			this(Optional.empty());
		}

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<DataComponentMap> bake(BakingContext context) {
			Minecraft minecraft = Minecraft.getInstance();
			return new MasonJarSpecialRenderer(
				defaultLid,
				minecraft.getItemModelResolver()
			);
		}
	}
}