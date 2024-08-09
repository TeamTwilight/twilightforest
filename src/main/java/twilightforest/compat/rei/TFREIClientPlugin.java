package twilightforest.compat.rei;

import dev.architectury.event.CompoundEventResult;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.entry.renderer.EntryRendererRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.DefaultSmithingDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.UncraftingScreen;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;
import twilightforest.compat.rei.categories.REIMoonwormQueenCategory;
import twilightforest.compat.rei.categories.REITransformationPowderCategory;
import twilightforest.compat.rei.categories.REIUncraftingCategory;
import twilightforest.compat.rei.displays.REICrumbleHornDisplay;
import twilightforest.compat.rei.displays.REITransformationPowderDisplay;
import twilightforest.compat.rei.displays.REIUncraftingDisplay;
import twilightforest.compat.rei.entries.EntityEntryDefinition;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.*;
import java.util.stream.Stream;

@REIPluginClient
public class TFREIClientPlugin implements REIClientPlugin {

	public static final EntityEntryDefinition ENTITY_DEFINITION = new EntityEntryDefinition();
	public Map<EntryStack<Entity>, EntryRenderer<Entity>> RENDER_CACHE = new WeakHashMap<>();

	@Override
	public void registerCategories(CategoryRegistry registry) {
		if (!TFConfig.disableEntireTable) {
			registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE));
			registry.addWorkstations(TFREIServerPlugin.UNCRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE));
		}
		registry.addWorkstations(REICrumbleHornCategory.CRUMBLE_HORN, EntryStacks.of(TFItems.CRUMBLE_HORN));
		registry.addWorkstations(REITransformationPowderCategory.TRANSFORMATION, EntryStacks.of(TFItems.TRANSFORMATION_POWDER));

		if (!TFConfig.disableEntireTable) {
			registry.add(new REIUncraftingCategory());
		}
		registry.add(new REICrumbleHornCategory());
		registry.add(new REITransformationPowderCategory());
		registry.add(new REIMoonwormQueenCategory());
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

		if (!TFConfig.disableEntireTable) {
			registry.registerRecipeFiller(UncraftingRecipe.class, TFRecipes.UNCRAFTING_RECIPE.get(), REIUncraftingDisplay::ofUncrafting);
			if (!TFConfig.disableUncraftingOnly) {
				registry.registerRecipeFiller(CraftingRecipe.class, RecipeType.CRAFTING, recipe -> {
					if (recipe.value().getResultItem(registryAccess).isEmpty() ||
						recipe.value().getResultItem(registryAccess).is(ItemTagGenerator.BANNED_UNCRAFTABLES) ||
						TFConfig.disableUncraftingRecipes.contains(recipe.id().toString()) ||
						TFConfig.flipUncraftingModIdList != TFConfig.blacklistedUncraftingModIds.contains(recipe.id().getNamespace())) {
						return null;
					}
					if (recipe.value() instanceof ShapelessRecipe && !TFConfig.allowShapelessUncrafting) {
						return null;
					}

					return REIUncraftingDisplay.of(recipe);
				});
			}
		}
		RecipeViewerConstants.getCrumbleHornRecipes().forEach(info ->
			registry.add(new REICrumbleHornDisplay(
				Collections.singletonList(EntryIngredients.of(info.getFirst().asItem())),
				Collections.singletonList(EntryIngredients.of(info.getSecond().asItem())),
				info.getSecond() == Blocks.AIR))
		);

		RecipeViewerConstants.getTransformationPowderRecipes().forEach(info -> registry.add(REITransformationPowderDisplay.of(info)));

		registry.add(REIMoonwormQueenCategory.createDisplay());
		registry.registerRecipeFiller(NoTemplateSmithingRecipe.class, RecipeType.SMITHING, holder -> new DefaultSmithingDisplay(holder.value(), holder.id(), List.of(EntryIngredients.of(ItemStack.EMPTY), EntryIngredients.ofIngredient(holder.value().getBase()), EntryIngredients.ofIngredient(holder.value().getAddition()))));
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		if (!TFConfig.disableEntireTable) {
			registry.registerClickArea(screen -> new Rectangle(34, 33, 27, 20), UncraftingScreen.class, TFREIServerPlugin.UNCRAFTING);
			registry.registerClickArea(screen -> new Rectangle(115, 33, 27, 20), UncraftingScreen.class, BuiltinPlugin.CRAFTING);
		}
	}

	@Override
	@SuppressWarnings("all") //I dont care if this is experimental
	public void registerEntryRenderers(EntryRendererRegistry registry) {
		RENDER_CACHE.clear();

		registry.register(EntityEntryDefinition.ENTITY_TYPE, (entry, last) -> {
			if (entry.getValue() instanceof ItemEntity) {
				return RENDER_CACHE.computeIfAbsent(entry, stack -> new EntityEntryDefinition.ItemEntityRenderer());
			}

			return last;
		});
	}

	@Override
	public void registerEntryTypes(EntryTypeRegistry registry) {
		registry.register(EntityEntryDefinition.ENTITY_TYPE, ENTITY_DEFINITION);

		registry.registerBridge(VanillaEntryTypes.ITEM, EntityEntryDefinition.ENTITY_TYPE, object -> {
			Optional<Stream<EntryStack<Entity>>> stream;

			ItemStack stack = object.getValue();

			try {
				Entity entity;

				if (stack.getItem() instanceof DeferredSpawnEggItem spawnEggItem) {
					EntityType<?> type = spawnEggItem.getType(stack);

					entity = EntityRenderingUtil.fetchEntity(type, Minecraft.getInstance().level);
				} else {
					entity = createItemEntity(stack);
				}

				stream = Optional.of(Stream.of(EntryStack.of(ENTITY_DEFINITION, entity)));
			} catch (Exception e) {
				stream = Optional.empty();
				TwilightForestMod.LOGGER.error("Caught an error assigning an entity to a stack!", e);
			}

			return stream.map(CompoundEventResult::interruptTrue).orElseGet(CompoundEventResult::pass);
		});

		registry.registerBridge(EntityEntryDefinition.ENTITY_TYPE, VanillaEntryTypes.ITEM, object -> {
			Optional<Stream<EntryStack<ItemStack>>> stream = Optional.empty();

			Entity entity = EntityRenderingUtil.fetchEntity(object.getValue().getType(), Minecraft.getInstance().level);

			ItemStack stack = null;

			if (entity instanceof ItemEntity itemEntity) {
				stack = itemEntity.getItem();
			} else if (entity != null) {
				Item spawnEggItem = DeferredSpawnEggItem.byId(entity.getType());

				if (spawnEggItem != null) stack = spawnEggItem.getDefaultInstance();
			}

			if (stack != null) {
				stream = Optional.of(Stream.of(EntryStacks.of(stack)));
			}

			return stream.map(CompoundEventResult::interruptTrue).orElseGet(CompoundEventResult::pass);
		});
	}

	@Nullable
	public static ItemEntity createItemEntity(ItemStack stack) {
		//unfortunately entity creation is required here.
		//If I pull from my cache the items all render as the same block/item, depending on what was last rendered on screen
		ItemEntity entity = EntityType.ITEM.create(Minecraft.getInstance().level);

		if (entity != null) {
			entity.setItem(stack);
			return entity;
		}
		return null;
	}
}
