package twilightforest.client.model.item;

import com.google.common.base.Suppliers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

// This is quite similar to SpecialModelWrapper, but allows for the block entity to be updated in the inventory without meeting any preconditions
public class AnimatedSpecialModelWrapper<T> implements ItemModel {
	private final SpecialModelRenderer<T> specialRenderer;
	private final ModelRenderProperties properties;
	private final Supplier<Vector3fc[]> extents;
	private final Matrix4fc transformation;

	public AnimatedSpecialModelWrapper(SpecialModelRenderer<T> specialRenderer, ModelRenderProperties properties, Matrix4fc transformation) {
		this.specialRenderer = specialRenderer;
		this.properties = properties;
		this.transformation = transformation;
		this.extents = Suppliers.memoize(() -> {
			Set<Vector3fc> results = new HashSet<>();
			specialRenderer.getExtents(results::add);
			return results.toArray(Vector3fc[]::new);
		});
	}

	@Override
	public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		output.appendModelIdentityElement(this);
		ItemStackRenderState.LayerRenderState layer = output.newLayer();
		output.setAnimated();
		if (item.hasFoil()) {
			ItemStackRenderState.FoilType foilType = ItemStackRenderState.FoilType.STANDARD;
			layer.setFoilType(foilType);
			output.appendModelIdentityElement(foilType);
		}
		T argument = this.specialRenderer.extractArgument(item);
		layer.setExtents(this.extents);
		layer.setLocalTransform(this.transformation);
		layer.setupSpecialModel(this.specialRenderer, argument);
		if (argument != null) {
			output.appendModelIdentityElement(argument);
		}
		this.properties.applyToLayer(layer, displayContext);
	}

	public record Unbaked(Identifier base, Optional<Transformation> transformation, SpecialModelRenderer.Unbaked<?> specialModel) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Identifier.CODEC.fieldOf("base")
					.forGetter(Unbaked::base),
				Transformation.EXTENDED_CODEC.optionalFieldOf("transformation")
					.forGetter(Unbaked::transformation),
				SpecialModelRenderers.CODEC.fieldOf("model")
					.forGetter(Unbaked::specialModel))
			.apply(instance, Unbaked::new));

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			resolver.markDependency(this.base);
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
			Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
			SpecialModelRenderer<?> bakedSpecialModel = this.specialModel.bake(context);
			if (bakedSpecialModel == null) {
				return context.missingItemModel(modelTransform);
			}
			ModelRenderProperties properties = this.getProperties(context);
			return new AnimatedSpecialModelWrapper<>(
				bakedSpecialModel,
				properties,
				modelTransform
			);
		}

		private ModelRenderProperties getProperties(ItemModel.BakingContext context) {
			ModelBaker baker = context.blockModelBaker();
			ResolvedModel model = baker.getModel(this.base);
			TextureSlots textureSlots = model.getTopTextureSlots();
			return ModelRenderProperties.fromResolvedModel(
				baker,
				model,
				textureSlots
			);
		}

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}
	}
}