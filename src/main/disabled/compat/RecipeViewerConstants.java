package twilightforest.compat;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import mezz.jei.library.plugins.vanilla.crafting.JeiShapedRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.config.TFConfig;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFDataMaps;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.util.datamaps.EntityTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeViewerConstants {
	public static final int GENERIC_RECIPE_WIDTH = 116;
	public static final int GENERIC_RECIPE_HEIGHT = 54;
	public static final Component MOONWORM_QUEEN_TOOLTIP = Component.translatable("item.twilightforest.moonworm_queen.jei_info_message").withStyle(ChatFormatting.GREEN);

	public static final ItemStack DAMAGED_MOONWORM_QUEEN = Util.make(new ItemStack(TFItems.MOONWORM_QUEEN.get()), stack -> stack.setDamageValue(256));
	//trickery is afoot
	public static final List<ItemStack> BERRY_2_LIST = List.of(ItemStack.EMPTY, new ItemStack(TFItems.TORCHBERRIES.get()), new ItemStack(TFItems.TORCHBERRIES.get()), new ItemStack(TFItems.TORCHBERRIES.get()));
	public static final List<ItemStack> BERRY_3_LIST = List.of(ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(TFItems.TORCHBERRIES.get()), new ItemStack(TFItems.TORCHBERRIES.get()));
	public static final List<ItemStack> BERRY_4_LIST = List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(TFItems.TORCHBERRIES.get()));
	public static final List<ItemStack> MOONWORM_QUEEN_LIST = List.of(
		Util.make(new ItemStack(TFItems.MOONWORM_QUEEN.get()), stack -> stack.setDamageValue(192)),
		Util.make(new ItemStack(TFItems.MOONWORM_QUEEN.get()), stack -> stack.setDamageValue(128)),
		Util.make(new ItemStack(TFItems.MOONWORM_QUEEN.get()), stack -> stack.setDamageValue(64)),
		new ItemStack(TFItems.MOONWORM_QUEEN.get()));

	public static List<RecipeHolder<? extends CraftingRecipe>> getAllUncraftingRecipes(RecipeManager manager) {
		if (!TFConfig.disableUncraftingOnly) { //we only do this if uncrafting is not disabled
			List<RecipeHolder<? extends CraftingRecipe>> recipes = new ArrayList<>(manager.getAllRecipesFor(RecipeType.CRAFTING));
			recipes = recipes.stream().filter(recipe ->
					!recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()).isEmpty() && //get rid of empty items
						!recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()).is(TFItemTags.BANNED_UNCRAFTABLES) && //Prevents things that are tagged as banned from showing up
						TFConfig.reverseRecipeBlacklist == TFConfig.disableUncraftingRecipes.contains(recipe.id().toString()) && //remove disabled recipes
						TFConfig.flipUncraftingModIdList == TFConfig.blacklistedUncraftingModIds.contains(recipe.id().getNamespace())) //remove blacklisted mod ids
				.collect(Collectors.toList());
			recipes.removeIf(recipe -> (recipe.value() instanceof ShapelessRecipe && !TFConfig.allowShapelessUncrafting));
			recipes.addAll(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
			return recipes;
		} else {
			return new ArrayList<>(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
		}
	}

	//all recipe viewers run this once when initializing recipes
	public static List<TransformationPowderInfo> getTransformationPowderRecipes() {
		List<EntityType<?>> inputs = new ArrayList<>();
		List<TransformationPowderInfo> info = new ArrayList<>();
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			if (type.builtInRegistryHolder().getData(TFDataMaps.TRANSFORMATION_POWDER) != null) {
				inputs.add(type);
			}
		}

		for (EntityType<?> input : new ArrayList<>(inputs)) {
			var output = input.builtInRegistryHolder().getData(TFDataMaps.TRANSFORMATION_POWDER);
			if (output != null) {
				TransformationPowderInfo dummy = new TransformationPowderInfo(output.result(), input, true);
				if (!info.contains(dummy)) {
					if (inputs.contains(output.result())) {
						info.add(new TransformationPowderInfo(input, output.result(), true));
					} else {
						info.add(new TransformationPowderInfo(input, output.result(), false));
					}
				}
			}
		}
		return info;
	}

	//all recipe viewers run this once when initializing recipes
	@SuppressWarnings("deprecation")
	public static List<OminousFireInfo> getOminousFireRecipes() {
		List<EntityType<?>> inputs = new ArrayList<>();
		List<OminousFireInfo> info = new ArrayList<>();
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			if (type.builtInRegistryHolder().getData(TFDataMaps.OMINOUS_FIRE) != null) {
				inputs.add(type);
			}
		}

		for (EntityType<?> input : new ArrayList<>(inputs)) {
            EntityTransformation output = input.builtInRegistryHolder().getData(TFDataMaps.OMINOUS_FIRE);
			if (output != null) {
				OminousFireInfo dummy = new OminousFireInfo(output.result(), input);
				if (!info.contains(dummy)) {
                    info.add(new OminousFireInfo(input, output.result()));
                }
			}
		}
		return info;
	}

	//all recipe viewers run this once when initializing recipes
	public static List<Pair<Block, Block>> getCrumbleHornRecipes() {
		List<Pair<Block, Block>> info = new ArrayList<>();
		for (Block input : BuiltInRegistries.BLOCK) {
			var output = input.builtInRegistryHolder().getData(TFDataMaps.CRUMBLE_HORN);
			if (output != null) {
				info.add(Pair.of(input, output.result()));
			}
		}
		return info;
	}

	public static int getRecipeCost(List<ItemStack> inputs) {
		int cost = 0;
		for (ItemStack stack : inputs) {
			if (UncraftingMenu.isDamageableComponent(stack) && !UncraftingMenu.isIngredientProblematic(stack) && !UncraftingMenu.isMarked(stack)) {
				cost++;
			}
		}
		return cost;
	}

	public static int getXPColor(int cost) {
		if (Minecraft.getInstance().player.experienceLevel < cost && !Minecraft.getInstance().player.getAbilities().instabuild) {
			return 0xA00000;
		} else {
			return 0x80FF20;
		}
	}

	public static Component getDryingTime(int dryingTicks) {
		int dryingMinutes = dryingTicks / 60 / 20;
		int dryingSeconds = (dryingTicks / 20) % 60;
		Component time;

		if (dryingMinutes > 0) {
			if (dryingSeconds > 0) {
				time = Component.translatable("gui.twilightforest.drying_time", dryingMinutes, dryingSeconds);
			} else {
				if (dryingMinutes == 1) {
					time = Component.translatable("gui.twilightforest.drying_minute", dryingMinutes);
				} else {
					time = Component.translatable("gui.twilightforest.drying_minutes", dryingMinutes);
				}
			}
		} else {
			if (dryingSeconds == 1) {
				time = Component.translatable("gui.twilightforest.drying_second", dryingSeconds);
			} else {
				time = Component.translatable("gui.twilightforest.drying_seconds", dryingSeconds);
			}
		}
		return time;
	}

	public static void renderFlatBlock(PoseStack stack, BlockState state, Vec3 location, float scale) {
		Minecraft minecraft = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		stack.pushPose();
		Lighting.setupForFlatItems();
		stack.translate(location.x(), location.y(), location.z());
		stack.scale(scale, -scale, scale);
		minecraft.getBlockRenderer().renderSingleBlock(state, stack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		stack.popPose();
		bufferSource.endBatch();
		Lighting.setupFor3DItems();
	}

	//copy of JEI's CraftingGridHelper.getCraftingIndex
	public static int getCraftingIndex(CraftingRecipe recipe, int i) {
		int width = getRecipeWidth(recipe);
		int height = getRecipeHeight(recipe);
		int index;
		if (width == 1) {
			if (height == 3) {
				index = (i * 3) + 1;
			} else if (height == 2) {
				index = (i * 3) + 1;
			} else {
				index = 4;
			}
		} else if (height == 1) {
			index = i + 3;
		} else if (width == 2) {
			index = i;
			if (i > 1) {
				index++;
				if (i > 3) {
					index++;
				}
			}
		} else if (height == 2) {
			index = i + 3;
		} else {
			index = i;
		}
		return index;
	}

	public static int getRecipeWidth(CraftingRecipe recipe) {
		if (recipe instanceof ShapedRecipe shapedRecipe) {
			return shapedRecipe.getWidth();
		}
		if (recipe instanceof JeiShapedRecipe shapedRecipe) {
			return shapedRecipe.getWidth();
		}
		return 0;
	}

	public static int getRecipeHeight(CraftingRecipe recipe) {
		if (recipe instanceof ShapedRecipe shapedRecipe) {
			return shapedRecipe.getHeight();
		}
		if (recipe instanceof JeiShapedRecipe shapedRecipe) {
			return shapedRecipe.getHeight();
		}
		return 0;
	}

	public record TransformationPowderInfo(EntityType<?> input, EntityType<?> output, boolean reversible) {
	}

	public record OminousFireInfo(EntityType<?> input, EntityType<?> output) {
	}
}
