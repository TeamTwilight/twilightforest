package twilightforest.compat.emi.recipes;

import com.google.common.collect.Lists;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiGrindstoneRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.compat.common.DefaultModifiedTravellersGearGetter;

import java.util.List;
import java.util.Random;

public class EmiTravellersGearGrindstoneRecipe extends EmiGrindstoneRecipe {
	protected ItemStack modifiedStack;

	public EmiTravellersGearGrindstoneRecipe(ItemStack modifiedStack) {
		super(modifiedStack.getItem(), TwilightForestMod.prefix("/" + modifiedStack.getItem().getDescriptionId()));
		this.modifiedStack = modifiedStack;
	}

	@Override
	protected EmiStack getItem(Random random, int item) {
		List<ItemStack> items = Lists.newArrayList();
		items.add(modifiedStack);
		items.add(ItemStack.EMPTY);
		items.add(DefaultModifiedTravellersGearGetter.getDemodifiedStack(modifiedStack));
		return EmiStack.of(items.get(item));
	}

	public static void register(EmiRegistry registry) {
		DefaultModifiedTravellersGearGetter.getDefaultModifiedTravellersGear(Minecraft.getInstance().level.registryAccess()).forEach(stack ->
			registry.addRecipe(new EmiTravellersGearGrindstoneRecipe(stack))
		);
	}
}
