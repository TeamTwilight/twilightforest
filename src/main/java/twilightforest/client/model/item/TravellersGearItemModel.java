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
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
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
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TravellersGearItemModel implements IUnbakedGeometry<TravellersGearItemModel> {

	private static final Function<Float, Transformation> TRANSFORM = f -> new Transformation(null, null, new Vector3f(1.0F + f), null);
	private final List<Holder.Reference<TravellersModifier>> modifiers;
	private final String directory;
	private final boolean broken;
	private final String brokenDirectory;
	private final boolean showGloves;

	TravellersGearItemModel(List<Holder.Reference<TravellersModifier>> modifiers, String directory, boolean broken, String brokenDirectory, boolean showGloves) {
		this.modifiers = modifiers;
		this.directory = directory;
		this.broken = broken;
		this.brokenDirectory = brokenDirectory;
		this.showGloves = showGloves;
	}

	public TravellersGearItemModel withModifiers(List<Holder.Reference<TravellersModifier>> modifiers, boolean broken, boolean showGloves) {
		return new TravellersGearItemModel(modifiers, this.directory, broken, this.brokenDirectory, showGloves);
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {

		var sprite = this.getSprite(context, spriteGetter);

		// We need to disable GUI 3D and block lighting for this to render properly
		var itemContext = StandaloneGeometryBakingContext.builder(context).withGui3d(false).withUseBlockLight(false).build(TwilightForestMod.prefix("travellers_gear"));
		var modelBuilder = CompositeModel.Baked.builder(itemContext, null, new TravellersGearItemModel.Overrides(overrides, this, baker, itemContext), context.getTransforms());

		var normalRenderTypes = DynamicFluidContainerModel.getLayerRenderTypes(false);

		if (sprite != null) {
			// Base texture
			var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, sprite);
			var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, modelState);
			modelBuilder.addQuads(normalRenderTypes, quads);
		}

		int layers = 1;
		for (Holder.Reference<TravellersModifier> modifier : this.modifiers) {
			var modSprite = this.getModifierSprite(modifier.key(), spriteGetter);
			if (!modSprite.contents().name().equals(MissingTextureAtlasSprite.getLocation())) {
				var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, modSprite);
				var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> modSprite, new SimpleModelState(modelState.getRotation().compose(TRANSFORM.apply(layers * 0.001F)), modelState.isUvLocked()));
				modelBuilder.addQuads(normalRenderTypes, quads);
				layers++;
			}
		}

		if (this.showGloves) {
			var modSprite = spriteGetter.apply(ClientHooks.getBlockMaterial(TwilightForestMod.prefix("item/" + (this.broken ? this.brokenDirectory : this.directory) + "gloves")));
			if (!modSprite.contents().name().equals(MissingTextureAtlasSprite.getLocation())) {
				var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, modSprite);
				var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> modSprite, new SimpleModelState(modelState.getRotation().compose(TRANSFORM.apply(layers * 0.001F)), modelState.isUvLocked()));
				modelBuilder.addQuads(normalRenderTypes, quads);
			}
		}

		modelBuilder.setParticle(sprite);

		return modelBuilder.build();
	}

	@Nullable
	private TextureAtlasSprite getSprite(IGeometryBakingContext context, Function<Material, TextureAtlasSprite> spriteGetter) {
		Material baseLocation = context.hasMaterial("base") ? context.getMaterial("base") : null;
		if (this.broken) {
			Material brokenLocation = context.hasMaterial("broken") ? context.getMaterial("broken") : null;
			return brokenLocation != null ? spriteGetter.apply(brokenLocation) : baseLocation != null ? spriteGetter.apply(baseLocation) : null;
		} else {
			return baseLocation != null ? spriteGetter.apply(baseLocation) : null;
		}
	}

	private TextureAtlasSprite getModifierSprite(ResourceKey<TravellersModifier> modifier, Function<Material, TextureAtlasSprite> spriteGetter) {
		return spriteGetter.apply(ClientHooks.getBlockMaterial(modifier.location().withPrefix("item/" + (this.broken ? this.brokenDirectory : this.directory))));
	}

	public static final class Loader implements IGeometryLoader<TravellersGearItemModel> {
		public static final TravellersGearItemModel.Loader INSTANCE = new TravellersGearItemModel.Loader();

		private Loader() {}

		@Override
		public TravellersGearItemModel read(JsonObject object, JsonDeserializationContext context) {
			return new TravellersGearItemModel(List.of(), GsonHelper.getAsString(object, "modifier_directory"), false, GsonHelper.getAsString(object, "broken_modifier_directory"), false);
		}
	}

	private static final class Overrides extends ItemOverrides {
		private final Map<String, BakedModel> possibleCombos = Maps.newHashMap();
		private final ItemOverrides nested;
		private final TravellersGearItemModel parent;
		private final ModelBaker baker;
		private final IGeometryBakingContext owner;

		private Overrides(ItemOverrides nested, TravellersGearItemModel parent, ModelBaker baker, IGeometryBakingContext owner) {
			this.nested = nested;
			this.parent = parent;
			this.baker = baker;
			this.owner = owner;
		}

		@Nullable
		@Override
		public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
			BakedModel overridden = this.nested.resolve(originalModel, stack, level, entity, seed);
			if (overridden != originalModel) return overridden;

			List<Holder.Reference<TravellersModifier>> modifiers = TravellersModifiersManager.findAllInsertableModifiers(level, stack);
			boolean broken = TravellersArmorItem.isTravellersArmorAndBroken(stack);
			boolean gloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
			String key = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath() + this.getModifiersSuffix(modifiers, broken, gloves);

			if (!this.possibleCombos.containsKey(key)) {
				TravellersGearItemModel unbaked = this.parent.withModifiers(modifiers, broken, gloves);
				BakedModel bakedModel = unbaked.bake(this.owner, this.baker, Material::sprite, BlockModelRotation.X0_Y0, this);
				this.possibleCombos.put(key, bakedModel);
				return bakedModel;
			}

			return this.possibleCombos.get(key);
		}

		private String getModifiersSuffix(List<Holder.Reference<TravellersModifier>> modifiers, boolean broken, boolean gloves) {
			StringBuilder ret = new StringBuilder();
			if (gloves) ret.append("_gloves");
			if (broken) ret.append("_broken");
			for (var mod : modifiers) {
				ret.append("_").append(mod.key().location().toLanguageKey());
			}
			return ret.toString();
		}
	}
}
