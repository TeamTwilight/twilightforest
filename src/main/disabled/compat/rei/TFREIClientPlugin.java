package twilightforest.compat.rei;

import dev.architectury.event.CompoundEventResult;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.filtering.FilteringRuleTypeRegistry;
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
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.UncraftingScreen;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.categories.*;
import twilightforest.compat.rei.displays.*;
import twilightforest.compat.rei.entries.BlockStateEntryDefinition;
import twilightforest.compat.rei.entries.EntityEntryDefinition;
import twilightforest.compat.rei.fillers.MoonwormQueenRepairFiller;
import twilightforest.compat.rei.fillers.REITravellersGearModifierRecipeFiller;
import twilightforest.compat.rei.filter.HideItemFilterType;
import twilightforest.config.TFConfig;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.DryingRecipe;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@REIPluginClient
public class TFREIClientPlugin implements REIClientPlugin {

	public static final EntityEntryDefinition ENTITY_DEFINITION = new EntityEntryDefinition();
	public static final BlockStateEntryDefinition BLOCKSTATE_DEFINITION = new BlockStateEntryDefinition();
	public Map<EntryStack<Entity>, EntryRenderer<Entity>> RENDER_CACHE = new WeakHashMap<>();

	static {
		FilteringRuleTypeRegistry.getInstance().register(TwilightForestMod.prefix("filter"), HideItemFilterType.INSTANCE);
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		if (!TFConfig.disableEntireTable) {
			registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE));
			registry.addWorkstations(TFREIServerPlugin.UNCRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE));
		}
		registry.addWorkstations(REICrumbleHornCategory.CRUMBLE_HORN, EntryStacks.of(TFItems.CRUMBLE_HORN));
		registry.addWorkstations(REITransformationPowderCategory.TRANSFORMATION, EntryStacks.of(TFItems.TRANSFORMATION_POWDER));
		registry.addWorkstations(REIOminousFireCategory.OMINOUS_FIRE, EntryStacks.of(TFItems.EXANIMATE_ESSENCE));
		registry.addWorkstations(REIDryingCategory.DRYING, EntryIngredients.ofItemTag(TFItemTags.DRYING_RACKS));

		if (!TFConfig.disableEntireTable) {
			registry.add(new REIUncraftingCategory());
		}
		registry.add(new REICrumbleHornCategory());
		registry.add(new REITransformationPowderCategory());
		registry.add(new REIOminousFireCategory());
		registry.add(new REIDryingCategory());
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

		if (!TFConfig.disableEntireTable) {
			registry.registerRecipeFiller(UncraftingRecipe.class, TFRecipes.UNCRAFTING_RECIPE.get(), REIUncraftingDisplay::ofUncrafting);
			if (!TFConfig.disableUncraftingOnly) {
				registry.registerRecipeFiller(CraftingRecipe.class, RecipeType.CRAFTING, recipe -> {
					if (recipe.value().getResultItem(registryAccess).isEmpty() ||
						recipe.value().getResultItem(registryAccess).is(TFItemTags.BANNED_UNCRAFTABLES) ||
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

		RecipeViewerConstants.getCrumbleHornRecipes().forEach(info -> registry.add(REICrumbleHornDisplay.of(info.getFirst(), info.getSecond())));
		RecipeViewerConstants.getTransformationPowderRecipes().forEach(info -> registry.add(REITransformationPowderDisplay.of(info)));
		RecipeViewerConstants.getOminousFireRecipes().forEach(info -> registry.add(REIOminousFireDisplay.of(info)));

		registry.registerRecipesFiller(NoTemplateSmithingRecipe.class, RecipeType.SMITHING, REINoTemplateDisplay::noTemplate);
		registry.registerRecipeFiller(DryingRecipe.class, TFRecipes.DRYING_RECIPE.get(), holder -> {
			if (!holder.value().getResult().is(TFItems.STALE_BREAD)) {
				return REIDryingDisplay.of(holder.value());
			}
			return null;
		});

		new REITravellersGearModifierRecipeFiller().registerDisplays(registry);
		new MoonwormQueenRepairFiller().registerDisplays(registry);
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		if (!TFConfig.disableEntireTable) {
			registry.registerClickArea(screen -> new Rectangle(34, 33, 27, 20), UncraftingScreen.class, TFREIServerPlugin.UNCRAFTING);
			registry.registerClickArea(screen -> new Rectangle(115, 33, 27, 20), UncraftingScreen.class, BuiltinPlugin.CRAFTING);
		}
	}

	@Override
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
		registry.register(BlockStateEntryDefinition.BLOCKSTATE, BLOCKSTATE_DEFINITION);

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

		registry.registerBridge(VanillaEntryTypes.ITEM, BlockStateEntryDefinition.BLOCKSTATE, object -> {
			Optional<Stream<EntryStack<BlockState>>> stream = Optional.empty();

			if (object.getValue().getItem() instanceof BlockItem block) {
				stream = Optional.of(Stream.of(EntryStack.of(BLOCKSTATE_DEFINITION, block.getBlock().defaultBlockState())));
			}

			return stream.map(CompoundEventResult::interruptTrue).orElseGet(CompoundEventResult::pass);
		});

		registry.registerBridge(BlockStateEntryDefinition.BLOCKSTATE, VanillaEntryTypes.ITEM, object -> {
			Optional<Stream<EntryStack<ItemStack>>> stream = Optional.empty();

			if (object.getValue().getBlock().asItem() != Items.AIR) {
				stream = Optional.of(Stream.of(EntryStacks.of(new ItemStack(object.getValue().getBlock()))));
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
