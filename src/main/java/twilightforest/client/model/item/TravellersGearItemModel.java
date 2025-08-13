package twilightforest.client.model.item;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.model.CompositeModel;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.geometry.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TravellersGearItemModel implements IUnbakedGeometry<TravellersGearItemModel> {

	private static final Function<Float, Transformation> TRANSFORM = f -> new Transformation(null, null, new Vector3f(1.0F + f), null);
	private final List<InsertableTravellersModifier> modifiers;

	TravellersGearItemModel(List<InsertableTravellersModifier> modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		Material baseLocation = context.hasMaterial("base") ? context.getMaterial("base") : null;

		TextureAtlasSprite baseSprite = baseLocation != null ? spriteGetter.apply(baseLocation) : null;

		// We need to disable GUI 3D and block lighting for this to render properly
		var itemContext = StandaloneGeometryBakingContext.builder(context).withGui3d(false).withUseBlockLight(false).build(TwilightForestMod.prefix("travellers_gear"));
		var modelBuilder = CompositeModel.Baked.builder(itemContext, baseSprite, new TravellersGearItemModel.Overrides(overrides, baker, itemContext), context.getTransforms());

		var normalRenderTypes = DynamicFluidContainerModel.getLayerRenderTypes(false);

		if (baseLocation != null) {
			// Base texture
			var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, baseSprite);
			var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> baseSprite, modelState);
			modelBuilder.addQuads(normalRenderTypes, quads);
		}

		int layers = 1;
		for (TravellersModifier modifier : this.modifiers) {
			var sprite = this.getModifierSprite(modifier, baseSprite, spriteGetter);
			if (!sprite.contents().name().equals(MissingTextureAtlasSprite.getLocation())) {
				var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, sprite);
				var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, new SimpleModelState(modelState.getRotation().compose(TRANSFORM.apply(layers * 0.001F)), modelState.isUvLocked()));
				modelBuilder.addQuads(normalRenderTypes, quads);
				layers++;
			}
		}

		modelBuilder.setParticle(baseSprite);

		return modelBuilder.build();
	}

	private TextureAtlasSprite getModifierSprite(TravellersModifier modifier, TextureAtlasSprite base, Function<Material, TextureAtlasSprite> spriteGetter) {
		return spriteGetter.apply(ClientHooks.getBlockMaterial(modifier.name().withPrefix("item/travellers_modifiers/" + this.sanitize(base.contents().name().getPath()) + "/")));
	}

	private String sanitize(String name) {
		return name
			.replace("travellers_", "")
			.replace("item/", "");
	}

	public static final class Loader implements IGeometryLoader<TravellersGearItemModel> {
		public static final TravellersGearItemModel.Loader INSTANCE = new TravellersGearItemModel.Loader();

		private Loader() {}

		@Override
		public TravellersGearItemModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
			return new TravellersGearItemModel(List.of());
		}
	}

	private static final class Overrides extends ItemOverrides {
		private final Map<String, BakedModel> possibleCombos = Maps.newHashMap();
		private final ItemOverrides nested;
		private final ModelBaker baker;
		private final IGeometryBakingContext owner;

		private Overrides(ItemOverrides nested, ModelBaker baker, IGeometryBakingContext owner) {
			this.nested = nested;
			this.baker = baker;
			this.owner = owner;
		}

		@Nullable
		@Override
		public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
			BakedModel overridden = this.nested.resolve(originalModel, stack, level, entity, seed);
			if (overridden != originalModel) return overridden;

			List<InsertableTravellersModifier> modifiers = TravellersModifiersManager.findAllInsertableModifiers(stack);
			String key = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath() + this.getModifiersSuffix(modifiers);

			if (!this.possibleCombos.containsKey(key)) {
				TravellersGearItemModel unbaked = new TravellersGearItemModel(modifiers);
				BakedModel bakedModel = unbaked.bake(owner, baker, Material::sprite, BlockModelRotation.X0_Y0, this);
				this.possibleCombos.put(key, bakedModel);
				return bakedModel;
			}

			return this.possibleCombos.get(key);
		}

		private String getModifiersSuffix(List<InsertableTravellersModifier> modifiers) {
			StringBuilder ret = new StringBuilder();
			for (var mod : modifiers) {
				ret.append("_").append(mod.name().toString());
			}
			return ret.toString();
		}
	}
}
