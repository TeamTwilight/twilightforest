package twilightforest.compat.jei.util;

import mezz.jei.api.recipe.vanilla.IJeiGrindstoneRecipe;
import mezz.jei.library.plugins.vanilla.grindstone.GrindstoneRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import twilightforest.compat.common.DefaultModifiedTravellersGearGetter;

import java.util.List;
import java.util.Objects;

public abstract class GrindstoneTravellersRecipesGetter {
	public static List<IJeiGrindstoneRecipe> getRecipes() {
		return DefaultModifiedTravellersGearGetter.getDefaultModifiedTravellersGear(Objects.requireNonNull(Minecraft.getInstance().level).registryAccess())
			.stream()
			.map(GrindstoneTravellersRecipesGetter::getGrindstoneRecipe)
			.toList();
	}

	private static IJeiGrindstoneRecipe getGrindstoneRecipe(ItemStack modifiedStack) {
		return new GrindstoneRecipe(
			List.of(modifiedStack),
			List.of(ItemStack.EMPTY),
			List.of(DefaultModifiedTravellersGearGetter.getDemodifiedStack(modifiedStack)),
			0, 0, null
		);
	}
}
